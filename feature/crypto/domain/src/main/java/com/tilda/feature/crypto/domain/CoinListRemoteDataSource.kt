package com.tilda.feature.crypto.domain

import com.tilda.core.domain.util.NetworkError
import com.tilda.core.domain.util.Result

interface CoinListRemoteDataSource {
    suspend fun getCoins(
        pageSize: Int,
        page: Int,
        sortBy: String,
        sortDirection: String
    ): Result<List<Coin>, NetworkError>
}