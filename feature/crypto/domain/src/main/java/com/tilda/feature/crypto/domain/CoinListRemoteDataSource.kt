package com.tilda.feature.crypto.domain

import com.tilda.core.domain.util.NetworkError
import com.tilda.core.domain.util.Result

interface CoinListRemoteDataSource {
    suspend fun getCoins(limit: Int, offset: Int): Result<List<Coin>, NetworkError>
}