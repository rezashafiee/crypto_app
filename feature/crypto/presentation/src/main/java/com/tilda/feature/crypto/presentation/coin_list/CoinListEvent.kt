package com.tilda.feature.crypto.presentation.coin_list

import com.tilda.core.domain.util.Error

sealed interface CoinListEvent {
    data class LoadCoinsError(val error: Error) : CoinListEvent
}