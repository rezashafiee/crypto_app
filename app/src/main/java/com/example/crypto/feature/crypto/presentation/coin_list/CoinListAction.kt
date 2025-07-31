package com.example.crypto.feature.crypto.presentation.coin_list

import com.example.crypto.feature.crypto.presentation.models.CoinUi

sealed interface CoinListAction {
    data class SelectCoin(val coinUi: CoinUi) : CoinListAction
}