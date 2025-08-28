package com.example.crypto.feature.crypto.domain

import com.example.crypto.core.data.db.model.CoinEntity
import kotlinx.coroutines.flow.Flow

interface CoinListLocalDataSource {
    fun getCoins(): Flow<List<Coin>>
    suspend fun insertCoins(coins: List<CoinEntity>)
    suspend fun clearCoins()
}