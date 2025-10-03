package com.tilda.feature.crypto.domain.repository

import androidx.paging.PagingData
import com.tilda.core.domain.util.NetworkError
import com.tilda.core.domain.util.Result
import com.tilda.feature.crypto.domain.model.Coin
import com.tilda.feature.crypto.domain.model.CoinPrice
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime

interface CoinRepository {
    fun getPagedCoins(): Flow<PagingData<Coin>>

    suspend fun getCoinsHistory(
        coinSymbol: String,
        end: ZonedDateTime
    ): Result<List<CoinPrice>, NetworkError>
}