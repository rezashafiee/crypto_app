package com.tilda.feature.crypto.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.tilda.core.data.db.model.CoinEntity
import com.tilda.core.domain.util.NetworkError
import com.tilda.core.domain.util.Result
import com.tilda.feature.crypto.data.datasource.CoinRemoteDataSource
import com.tilda.feature.crypto.data.mapper.toCoin
import com.tilda.feature.crypto.domain.model.Coin
import com.tilda.feature.crypto.domain.model.CoinPrice
import com.tilda.feature.crypto.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.ZonedDateTime

class CoinRepositoryImp(
    private val pager: Pager<Int, CoinEntity>,
    private val coinRemoteDataSource: CoinRemoteDataSource
) : CoinRepository {

    override fun getPagedCoins(): Flow<PagingData<Coin>> {
        return pager.flow.map { pagingData ->
            pagingData.map { coinEntity -> coinEntity.toCoin() }
        }
    }

    override suspend fun getCoinsHistory(
        coinSymbol: String,
        end: ZonedDateTime
    ): Result<List<CoinPrice>, NetworkError> {
        return coinRemoteDataSource.getCoinHistory(coinSymbol, end)
    }
}