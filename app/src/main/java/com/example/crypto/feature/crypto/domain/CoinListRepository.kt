package com.example.crypto.feature.crypto.domain

import kotlinx.coroutines.flow.Flow

interface CoinListRepository {
    suspend fun getCoins(): Flow<List<Coin>>
}