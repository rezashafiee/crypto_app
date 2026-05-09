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

    /** Returns the ids of coins saved in the user's watchlist. */
    fun getFavoriteCoinIds(): Flow<Set<Int>>

    /** Persists or clears a favorite flag for the coin with [coinId]. */
    suspend fun setCoinFavorite(coinId: Int, isFavorite: Boolean)

    /**
     * Returns hourly price history for a specific coin symbol up to [end].
     */
    suspend fun getCoinHistory(
        coinSymbol: String,
        end: ZonedDateTime
    ): Result<List<CoinPrice>, NetworkError>
}
