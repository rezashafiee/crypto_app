package com.tilda.feature.crypto.data.local

import androidx.room.withTransaction
import com.tilda.core.data.db.CoinDatabase
import com.tilda.core.data.db.model.CoinEntity
import com.tilda.core.data.db.model.FavoriteCoinEntity
import com.tilda.feature.crypto.data.datasource.CoinLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CoinLocalDataSourceImp @Inject constructor(
    private val coinDatabase: CoinDatabase
) : CoinLocalDataSource {

    private val coinDao = coinDatabase.coinDao()
    private val favoriteCoinDao = coinDatabase.favoriteCoinDao()

    override suspend fun getItemsCount(): Int {
        return coinDao.getCoinCount()
    }

    override suspend fun getLastUpdated(): Long {
        return coinDao.getLastUpdatedEpochSeconds()
    }

    override suspend fun replaceAllCoins(coinEntities: List<CoinEntity>) {
        coinDatabase.withTransaction {
            coinDao.deleteAllCoins()
            coinDao.insertCoins(*coinEntities.toTypedArray())
        }
    }

    override suspend fun addCoins(coinEntities: List<CoinEntity>) {
        coinDao.insertCoins(*coinEntities.toTypedArray())
    }

    override fun getFavoriteCoinIds(): Flow<List<Int>> {
        return favoriteCoinDao.getFavoriteCoinIds()
    }

    override suspend fun addFavoriteCoin(coinId: Int) {
        favoriteCoinDao.insertFavoriteCoin(FavoriteCoinEntity(coinId))
    }

    override suspend fun deleteFavoriteCoin(coinId: Int) {
        favoriteCoinDao.deleteFavoriteCoin(coinId)
    }
}
