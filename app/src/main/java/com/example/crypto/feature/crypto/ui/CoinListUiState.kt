package com.example.crypto.feature.crypto.ui

import com.example.crypto.feature.crypto.ui.models.CoinUi

data class CoinListUiState(
    val isLoading: Boolean = false,
    val coins: List<CoinUi> = emptyList(),
    val selectedCoin: CoinUi? = null
)