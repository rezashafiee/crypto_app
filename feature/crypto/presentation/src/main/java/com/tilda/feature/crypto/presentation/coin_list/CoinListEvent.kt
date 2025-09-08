package com.tilda.feature.crypto.presentation.coin_list

import com.tilda.core.domain.util.DomainError

sealed interface CoinListEvent {
    data class LoadCoinsError(val error: DomainError) : CoinListEvent
}