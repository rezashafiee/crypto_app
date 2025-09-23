package com.tilda.feature.crypto.data.datasource

import com.tilda.core.data.db.model.CoinEntity

interface CoinListLocalDataSource {
    suspend fun getItemsCount(): Int

    suspend fun getLastUpdated(): Long

    suspend fun replaceAllCoins(coinEntities: List<CoinEntity>)

    suspend fun addCoins(coinEntities: List<CoinEntity>)
}