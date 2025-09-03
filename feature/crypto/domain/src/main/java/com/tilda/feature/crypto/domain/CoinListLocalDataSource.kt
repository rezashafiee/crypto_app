package com.tilda.feature.crypto.domain

import kotlinx.coroutines.flow.Flow

interface CoinListLocalDataSource {
    fun getCoins(): Flow<List<Coin>>
    suspend fun insertCoins(coins: List<Coin>)
    suspend fun clearCoins()
}