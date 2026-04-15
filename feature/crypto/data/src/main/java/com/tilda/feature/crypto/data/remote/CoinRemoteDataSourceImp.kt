package com.tilda.feature.crypto.data.remote

import com.tilda.core.data.network.safeRetrofitCall
import com.tilda.core.domain.util.NetworkError
import com.tilda.core.domain.util.Result
import com.tilda.feature.crypto.data.datasource.CoinRemoteDataSource
import com.tilda.feature.crypto.data.mapper.toCoin
import com.tilda.feature.crypto.data.mapper.toCoinPrice
import com.tilda.feature.crypto.domain.model.Coin
import com.tilda.feature.crypto.domain.model.CoinPrice
import java.time.ZonedDateTime

class CoinRemoteDataSourceImp(
    private val coinApiService: CoinApiService
) : CoinRemoteDataSource {

    private companion object {
        const val HISTORY_MARKET = "cadli"
        const val HISTORY_LIMIT = 24
    }

    override suspend fun getCoins(
        pageSize: Int,
        page: Int,
        sortBy: String,
        sortDirection: String
    ): Result<List<Coin>, NetworkError> {
        return safeRetrofitCall(
            execute = {
                coinApiService.getCoins(
                    pageSize = pageSize,
                    page = page,
                    sortBy = sortBy,
                    sortDirection = sortDirection
                )
            },
            mapper = { body -> body.data.list.map { it.toCoin() } }
        )
    }

    override suspend fun getCoinHistory(
        coinSymbol: String,
        end: ZonedDateTime
    ): Result<List<CoinPrice>, NetworkError> {
        return safeRetrofitCall(
            execute = {
                coinApiService.getCoinHistory(
                    market = HISTORY_MARKET,
                    instrument = "${coinSymbol.uppercase()}-USD",
                    limit = HISTORY_LIMIT,
                    toTs = end.toEpochSecond()
                )
            },
            mapper = { body -> body.data.map { it.toCoinPrice() } }
        )
    }
}