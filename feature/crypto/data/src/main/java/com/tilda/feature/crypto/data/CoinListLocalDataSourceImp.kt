package com.tilda.feature.crypto.data

import com.tilda.feature.crypto.data.mappers.toCoin
import com.tilda.feature.crypto.data.mappers.toCoinEntity
import com.tilda.feature.crypto.domain.Coin
import com.tilda.feature.crypto.domain.CoinListLocalDataSource
import com.tilda.core.data.db.dao.CoinDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CoinListLocalDataSourceImp(
    private val coinDao: CoinDao
) : CoinListLocalDataSource {
    override fun getCoins(): Flow<List<Coin>> {
        return coinDao.getAllCoins().map { coinEntities -> coinEntities.map { it.toCoin() } }
    }

    override suspend fun insertCoins(coins: List<Coin>) {
        coinDao.addCoins(*coins.map { it.toCoinEntity() }.toTypedArray())
    }

    override suspend fun clearCoins() {
        coinDao.removeCoins()
    }
}