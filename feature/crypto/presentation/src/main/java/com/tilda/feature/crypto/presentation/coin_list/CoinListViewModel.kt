package com.tilda.feature.crypto.presentation.coin_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.tilda.core.domain.util.onError
import com.tilda.core.domain.util.onSuccess
import com.tilda.feature.crypto.domain.interactor.GetFavoriteCoinIdsUseCase
import com.tilda.feature.crypto.domain.interactor.GetCoinHistoryUseCase
import com.tilda.feature.crypto.domain.interactor.GetPagedCoinsUseCase
import com.tilda.feature.crypto.domain.interactor.SetCoinFavoriteUseCase
import com.tilda.feature.crypto.presentation.models.CoinUi
import com.tilda.feature.crypto.presentation.models.toCoinUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    getPagedCoinsUseCase: GetPagedCoinsUseCase,
    getFavoriteCoinIdsUseCase: GetFavoriteCoinIdsUseCase,
    private val getCoinHistoryUseCase: GetCoinHistoryUseCase,
    private val setCoinFavoriteUseCase: SetCoinFavoriteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CoinListUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<CoinListEvent>()
    val events = _events.receiveAsFlow()

    private val showFavoritesOnly = MutableStateFlow(false)

    private val favoriteCoinIds = getFavoriteCoinIdsUseCase()
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptySet()
        )

    init {
        val pagedCoins =
            try {
                // Cache before combining with UI filters so re-emitting one PagingData
                // generation for watchlist changes remains safe to collect.
                val cachedPagedCoins = getPagedCoinsUseCase()
                    .cachedIn(viewModelScope)

                combine(
                    cachedPagedCoins,
                    favoriteCoinIds,
                    showFavoritesOnly
                ) { pagingData, favoriteIds, showFavoritesOnly ->
                    pagingData
                        .map { domainCoin ->
                            domainCoin.copy(isFavorite = favoriteIds.contains(domainCoin.id))
                        }
                        .filter { domainCoin -> !showFavoritesOnly || domainCoin.isFavorite }
                        .map { domainCoin -> domainCoin.toCoinUi() }
                }
                    .flowOn(Dispatchers.Default)
                    .cachedIn(viewModelScope)
            } catch (e: Exception) {
                e.printStackTrace()
                emptyFlow()
            }

        _state.update { currentState ->
            currentState.copy(
                pagedCoins = pagedCoins
            )
        }

        viewModelScope.launch {
            favoriteCoinIds.collect { favoriteIds ->
                _state.update { currentState ->
                    currentState.copy(
                        selectedCoin = currentState.selectedCoin?.let { selectedCoin ->
                            selectedCoin.copy(
                                isFavorite = favoriteIds.contains(selectedCoin.id)
                            )
                        }
                    )
                }
            }
        }
    }

    fun onCoinClicked(coinUi: CoinUi) {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    selectedCoin = coinUi
                )
            }
            getCoinHistoryUseCase(
                coinSymbol = coinUi.symbol,
                end = ZonedDateTime.now()
            )
                .onSuccess { history ->
                    _state.update {
                        it.copy(
                            selectedCoin = it.selectedCoin?.copy(
                                coinPriceHistory = history
                            )
                        )
                    }
                }
                .onError { error ->
                    _events.send(CoinListEvent.LoadCoinsError(error))
                }
        }
    }

    fun onFavoriteClick(coinUi: CoinUi) {
        viewModelScope.launch {
            setCoinFavoriteUseCase(
                coinId = coinUi.id,
                isFavorite = !coinUi.isFavorite
            )
        }
    }

    fun onFavoritesFilterChanged(showFavoritesOnly: Boolean) {
        this.showFavoritesOnly.value = showFavoritesOnly
        _state.update { currentState ->
            currentState.copy(showFavoritesOnly = showFavoritesOnly)
        }
    }
}
