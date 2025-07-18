package com.example.crypto.feature.crypto.domain

import com.example.crypto.core.domain.util.NetworkError
import com.example.crypto.core.domain.util.Result

interface CoinListDataSource {
    suspend fun getCoins(): Result<List<Coin>, NetworkError>
}