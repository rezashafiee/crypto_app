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
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class CoinsRemoteMediator(
    private val remoteDataSource: CoinListRemoteDataSource,
    private val coinDatabase: CoinDatabase
) : RemoteMediator<Int, CoinEntity>() {

    private val coinDao = coinDatabase.coinDao()
    private val sortBy = "TOTAL_MKT_CAP_USD"
    private val sortDirection = "DESC"

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CoinEntity>
    ): MediatorResult {

        val pageSize = state.config.pageSize

        return try {
            val pageNumber = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val totalItems = coinDao.countItems()
                    val nextPage = totalItems / pageSize + 1
                    nextPage
                }
            }

            when (val apiResult =
                remoteDataSource.getCoins(
                    pageSize = pageSize,
                    page = pageNumber,
                    sortBy = sortBy,
                    sortDirection = sortDirection
                )) {
                is Result.Success -> {
                    val coins = apiResult.data
                    val endOfPaginationReached = coins.isEmpty() || coins.size < pageSize

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

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS
            .convert(
                1L, TimeUnit.HOURS
            )
        val currentTime = System.currentTimeMillis()
        val lastUpdated = TimeUnit.MILLISECONDS
            .convert(
                coinDao.getLastUpdated(), TimeUnit.SECONDS
            )
        val passedTime = currentTime - lastUpdated

        return if (passedTime <= cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }
}
