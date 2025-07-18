package com.example.crypto.feature.crypto.data

import com.example.crypto.BuildConfig
import com.example.crypto.core.data.network.constructUrl
import com.example.crypto.core.data.network.safeCall
import com.example.crypto.core.domain.util.NetworkError
import com.example.crypto.core.domain.util.Result
import com.example.crypto.core.domain.util.map
import com.example.crypto.feature.crypto.data.mappers.toCoin
import com.example.crypto.feature.crypto.domain.Coin
import com.example.crypto.feature.crypto.domain.CoinListDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class CoinListRemoteDataSource(
    private val httpClient: HttpClient
) : CoinListDataSource {
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