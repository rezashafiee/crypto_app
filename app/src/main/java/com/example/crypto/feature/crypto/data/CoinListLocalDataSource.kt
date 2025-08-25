package com.example.crypto.feature.crypto.data

import com.example.crypto.core.domain.util.NetworkError
import com.example.crypto.core.domain.util.Result
import com.example.crypto.feature.crypto.domain.Coin
import com.example.crypto.feature.crypto.domain.CoinListDataSource

class CoinListLocalDataSource: CoinListDataSource {
    override suspend fun getCoins(): Result<List<Coin>, NetworkError> {
        TODO("Not yet implemented")
    }
}