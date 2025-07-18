package com.example.crypto.feature.crypto.ui

import com.example.crypto.core.domain.util.Error

sealed interface CoinListEvent {
    data class LoadCoinsError(val error: Error) : CoinListEvent
}