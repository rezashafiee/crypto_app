package com.tilda.feature.crypto.data.remote

import com.tilda.core.data.network.constructUrl
import com.tilda.core.data.network.safeCall
import com.tilda.core.domain.util.NetworkError
import com.tilda.core.domain.util.Result
import com.tilda.core.domain.util.map
import com.tilda.feature.crypto.data.datasource.CoinRemoteDataSource
import com.tilda.feature.crypto.data.dto.CoinHistoryResponse
import com.tilda.feature.crypto.data.dto.CoinListResponse
import com.tilda.feature.crypto.data.mapper.toCoin
import com.tilda.feature.crypto.data.mapper.toCoinPrice
import com.tilda.feature.crypto.domain.model.Coin
import com.tilda.feature.crypto.domain.model.CoinPrice
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import java.time.ZonedDateTime

class CoinRemoteDataSourceImp(
    private val httpClient: HttpClient
) : CoinRemoteDataSource {

    override suspend fun getCoins(
        pageSize: Int,
        page: Int,
        sortBy: String,
        sortDirection: String
    ): Result<List<Coin>, NetworkError> {
        return safeCall<CoinListResponse> {
            httpClient.get(urlString = constructUrl("/asset/v1/top/list")) {
                url.parameters.append("page_size", pageSize.toString())
                url.parameters.append("page", page.toString())
                url.parameters.append("sort_by", sortBy)
                url.parameters.append("sort_direction", sortDirection)
            }
        }.map { response ->
            response.data.list.map { it.toCoin() }
        }
    }

    override suspend fun getCoinHistory(
        coinSymbol: String,
        end: ZonedDateTime
    ): Result<List<CoinPrice>, NetworkError> {
        return safeCall<CoinHistoryResponse> {
            httpClient.get(urlString = constructUrl("/index/cc/v1/historical/hours")) {
                url.parameters.append("market", "cadli")
                url.parameters.append("instrument", "${coinSymbol.uppercase()}-USD")
                url.parameters.append("limit", "24")
                url.parameters.append("to_ts", end.toEpochSecond().toString())
            }
        }.map { response ->
            response.data.map { it.toCoinPrice() }
        }
    }
}