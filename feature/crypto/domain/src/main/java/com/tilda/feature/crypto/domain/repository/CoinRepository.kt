package com.tilda.feature.crypto.domain.repository

import androidx.paging.PagingData
import com.tilda.core.domain.util.NetworkError
import com.tilda.core.domain.util.Result
import com.tilda.feature.crypto.domain.model.Coin
import com.tilda.feature.crypto.domain.model.CoinPrice
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime

interface CoinRepository {
    /** Returns paged coins as a [Flow] to support incremental loading. */
    fun getPagedCoins(): Flow<PagingData<Coin>>

    /**
     * Returns hourly price history for a specific coin symbol up to [end].
     */
    suspend fun getCoinHistory(
        coinSymbol: String,
        end: ZonedDateTime
    ): Result<List<CoinPrice>, NetworkError>
}
