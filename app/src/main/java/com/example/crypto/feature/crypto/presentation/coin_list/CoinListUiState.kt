package com.example.crypto.feature.crypto.presentation.coin_list

import com.example.crypto.feature.crypto.presentation.models.CoinUi

data class CoinListUiState(
    val isLoading: Boolean = false,
    val coins: List<CoinUi> = emptyList(),
    val selectedCoin: CoinUi? = null
)