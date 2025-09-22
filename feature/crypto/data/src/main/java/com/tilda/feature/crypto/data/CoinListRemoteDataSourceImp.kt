package com.tilda.feature.crypto.data

import com.tilda.core.data.network.constructUrl
import com.tilda.core.data.network.safeCall
import com.tilda.core.domain.util.NetworkError
import com.tilda.core.domain.util.Result
import com.tilda.core.domain.util.map
import com.tilda.feature.crypto.data.mappers.toCoin
import com.tilda.feature.crypto.domain.Coin
import com.tilda.feature.crypto.domain.CoinListRemoteDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class CoinListRemoteDataSourceImp(
    private val httpClient: HttpClient
) : CoinListRemoteDataSource {
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

}