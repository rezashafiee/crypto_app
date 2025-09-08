package com.tilda.feature.crypto.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.paging.RemoteMediator.MediatorResult.Error
import androidx.paging.RemoteMediator.MediatorResult.Success
import androidx.room.withTransaction
import com.tilda.core.data.db.CoinDatabase
import com.tilda.core.data.db.model.CoinEntity
import com.tilda.core.domain.util.Result
import com.tilda.feature.crypto.data.mappers.toCoinEntity
import com.tilda.feature.crypto.domain.CoinListRemoteDataSource

@OptIn(ExperimentalPagingApi::class)
class CoinsRemoteMediator(
    private val remoteDataSource: CoinListRemoteDataSource,
    private val coinDatabase: CoinDatabase
) : RemoteMediator<Int, CoinEntity>() {

    private val coinDao = coinDatabase.coinDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CoinEntity>
    ): MediatorResult {
        return try {
            val loadKeyOffset = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    /*if (state.lastItemOrNull() == null) {
                        return Success(endOfPaginationReached = true)
                    }*/

                    val numberOfItems = state.pages.sumOf { page -> page.data.size }

                    if (numberOfItems == 0) {
                        return Success(endOfPaginationReached = true)
                    } else {
                        numberOfItems
                    }
                }
            }

            val limit = state.config.pageSize

            when (val apiResult =
                remoteDataSource.getCoins(limit = limit, offset = loadKeyOffset)) {
                is Result.Success -> {
                    val coins = apiResult.data
                    val endOfPaginationReached = coins.isEmpty() || coins.size < limit

                    coinDatabase.withTransaction {
                        if (loadType == LoadType.REFRESH) {
                            coinDao.removeAllCoins()
                        }
                        val coinEntities = coins.map { it.toCoinEntity() }
                        coinDao.addCoins(*coinEntities.toTypedArray())
                    }
                    Success(endOfPaginationReached = endOfPaginationReached)
                }

                is Result.Error -> {
                    val error = apiResult.error
                    Error(error)
                }
            }

        } catch (e: Exception) {
            Error(e)
        }
    }
}
