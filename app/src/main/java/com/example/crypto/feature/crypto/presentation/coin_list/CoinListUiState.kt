package com.example.crypto.feature.crypto.presentation.coin_list

import androidx.compose.runtime.Immutable
import com.example.crypto.feature.crypto.presentation.models.CoinUi

@Immutable
data class CoinListUiState(
    val isLoading: Boolean = false,
    val coins: List<CoinUi> = emptyList(),
    val selectedCoin: CoinUi? = null
)