package com.tilda.feature.crypto.data.datasource

import com.tilda.core.domain.util.NetworkError
import com.tilda.core.domain.util.Result
import com.tilda.feature.crypto.domain.model.Coin

interface CoinListRemoteDataSource {
    suspend fun getCoins(
        pageSize: Int,
        page: Int,
        sortBy: String,
        sortDirection: String
    ): Result<List<Coin>, NetworkError>
}