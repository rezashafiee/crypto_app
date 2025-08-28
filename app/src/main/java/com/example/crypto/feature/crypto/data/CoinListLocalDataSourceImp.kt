package com.example.crypto.feature.crypto.data

import com.example.crypto.core.data.db.dao.CoinDao
import com.example.crypto.core.data.db.model.CoinEntity
import com.example.crypto.feature.crypto.data.mappers.toCoin
import com.example.crypto.feature.crypto.domain.Coin
import com.example.crypto.feature.crypto.domain.CoinListLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CoinListLocalDataSourceImp(
    private val coinDao: CoinDao
) : CoinListLocalDataSource {
    override fun getCoins(): Flow<List<Coin>> {
        return coinDao.getAllCoins().map { coinEntities -> coinEntities.map { it.toCoin() } }
    }

    override suspend fun insertCoins(coins: List<CoinEntity>) {
        coinDao.addCoins(*coins.toTypedArray())
    }

    override suspend fun clearCoins() {
        coinDao.removeCoins()
    }
}