package com.tilda.feature.crypto.presentation.coin_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.tilda.feature.crypto.domain.CoinListRepository
import com.tilda.feature.crypto.presentation.models.CoinUi
import com.tilda.feature.crypto.presentation.models.toCoinUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CoinListViewModel(
    coinListRepository: CoinListRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CoinListUiState())
    val state = _state.asStateFlow()

    init {
        val pagedCoins =
            try {
                coinListRepository.getPagedDomainCoins()
                    .map { pagingData -> pagingData.map { domainCoin -> domainCoin.toCoinUi() } }
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
}