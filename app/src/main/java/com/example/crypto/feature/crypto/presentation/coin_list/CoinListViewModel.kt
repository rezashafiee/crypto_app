package com.example.crypto.feature.crypto.presentation.coin_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crypto.core.domain.util.onError
import com.example.crypto.core.domain.util.onSuccess
import com.example.crypto.feature.crypto.domain.CoinListDataSource
import com.example.crypto.feature.crypto.presentation.coin_list.CoinListUiState
import com.example.crypto.feature.crypto.presentation.models.toCoinUi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CoinListViewModel(
    val coinListDataSource: CoinListDataSource
): ViewModel() {
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

    private fun loadCoins() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            coinListDataSource.getCoins()
                .onSuccess { coins ->
                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            coins = coins.map { coin -> coin.toCoinUi() }
                        )
                    }
                }
                .onError { error ->
                    _event.emit(CoinListEvent.LoadCoinsError(error))
                }
        }
    }

}