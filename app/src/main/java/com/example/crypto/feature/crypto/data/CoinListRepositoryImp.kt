package com.example.crypto.feature.crypto.data

import com.example.crypto.feature.crypto.domain.Coin
import com.example.crypto.feature.crypto.domain.CoinListLocalDataSource
import com.example.crypto.feature.crypto.domain.CoinListRemoteDataSource
import com.example.crypto.feature.crypto.domain.CoinListRepository
import kotlinx.coroutines.flow.Flow

class CoinListRepositoryImp(
    private val remoteDataSource: CoinListRemoteDataSource,
    private val localDataSource: CoinListLocalDataSource
): CoinListRepository {

    override suspend fun getCoins(): Flow<List<Coin>> {
        TODO("Not yet implemented")
    }
}