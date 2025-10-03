package com.tilda.feature.crypto.data.local

import androidx.room.withTransaction
import com.tilda.core.data.db.CoinDatabase
import com.tilda.core.data.db.model.CoinEntity
import com.tilda.feature.crypto.data.datasource.CoinLocalDataSource

class CoinLocalDataSourceImp(
    private val coinDatabase: CoinDatabase
) : CoinLocalDataSource {

    private val coinDao = coinDatabase.coinDao()

    override suspend fun getItemsCount(): Int {
        return coinDao.countItems()
    }

    override suspend fun getLastUpdated(): Long {
        return coinDao.getLastUpdated()
    }

    override suspend fun replaceAllCoins(coinEntities: List<CoinEntity>) {
        coinDatabase.withTransaction {
            coinDao.removeAllCoins()
            coinDao.addCoins(*coinEntities.toTypedArray())
        }
    }

    override suspend fun addCoins(coinEntities: List<CoinEntity>) {
        coinDao.addCoins(*coinEntities.toTypedArray())
    }
}