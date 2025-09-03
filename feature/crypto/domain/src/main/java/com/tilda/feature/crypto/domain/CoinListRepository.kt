package com.tilda.feature.crypto.domain

import com.tilda.core.domain.util.RemoteSyncResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface CoinListRepository {
    val remoteSyncEvents: SharedFlow<RemoteSyncResult>
    suspend fun getCoins(): Flow<List<Coin>>
}