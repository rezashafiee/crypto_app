package com.tilda.feature.crypto.presentation.coin_list

import com.tilda.feature.crypto.presentation.models.CoinUi

internal sealed interface CoinListAction {
    data class SelectCoin(val coinUi: CoinUi) : CoinListAction
}
