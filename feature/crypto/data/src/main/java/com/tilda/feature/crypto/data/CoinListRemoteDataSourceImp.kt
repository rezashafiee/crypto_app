package com.tilda.feature.crypto.data

import com.tilda.feature.crypto.data.mappers.toCoin
import com.tilda.feature.crypto.domain.Coin
import com.tilda.feature.crypto.domain.CoinListRemoteDataSource
import com.tilda.core.domain.util.NetworkError
import com.tilda.core.domain.util.Result
import com.tilda.core.domain.util.map
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import com.tilda.core.data.network.constructUrl
import com.tilda.core.data.network.safeCall

class CoinListRemoteDataSourceImp(
    private val httpClient: HttpClient
) : CoinListRemoteDataSource {
    override suspend fun getCoins(): Result<List<Coin>, NetworkError> {
        return safeCall<CoinListResponse> {
            httpClient.get(urlString = constructUrl("/assets")) {
                url.parameters.append(
                    "apiKey",
                    BuildConfig.API_KEY
                )
            }
        }.map { response ->
            response.data.map { it.toCoin() }
        }
    }

}