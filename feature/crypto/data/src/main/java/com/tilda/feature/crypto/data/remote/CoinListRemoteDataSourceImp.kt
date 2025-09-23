package com.tilda.feature.crypto.data.remote

import com.tilda.core.data.network.constructUrl
import com.tilda.core.data.network.safeCall
import com.tilda.core.domain.util.NetworkError
import com.tilda.core.domain.util.Result
import com.tilda.core.domain.util.map
import com.tilda.feature.crypto.data.dto.CoinListResponse
import com.tilda.feature.crypto.data.datasource.CoinListRemoteDataSource
import com.tilda.feature.crypto.data.mapper.toCoin
import com.tilda.feature.crypto.domain.model.Coin
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