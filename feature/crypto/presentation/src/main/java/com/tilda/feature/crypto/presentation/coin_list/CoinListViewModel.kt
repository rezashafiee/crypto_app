package com.tilda.feature.crypto.presentation.coin_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tilda.feature.crypto.domain.CoinListRepository
import com.tilda.core.domain.util.RemoteSyncResult
import com.tilda.feature.crypto.presentation.models.CoinUi
import com.tilda.feature.crypto.presentation.models.toCoinUi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CoinListViewModel(
    val coinListRepository: CoinListRepository
) : ViewModel() {
    private val _state = MutableStateFlow(CoinListUiState())
    val state = _state
        .onStart { loadCoins() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000L),
            initialValue = CoinListUiState()
        )

    private val _event = MutableSharedFlow<CoinListEvent>()
    val event = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            coinListRepository.remoteSyncEvents
                .collect { remoteSyncResult ->
                    if (remoteSyncResult is RemoteSyncResult.Error)
                        _event.emit(CoinListEvent.LoadCoinsError(remoteSyncResult.error))
                }
        }
    }

    fun onCoinClicked(coin: CoinUi) {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    selectedCoin = coin
                )
            }
        }
    }

    private fun loadCoins() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            coinListRepository.getCoins()
                .collect { coins ->
                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            coins = coins.map { coin -> coin.toCoinUi() }
                        )
                    }
                }
        }
    }
}