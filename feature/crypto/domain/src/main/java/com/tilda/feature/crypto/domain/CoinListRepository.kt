package com.tilda.feature.crypto.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface CoinListRepository {
    fun getPagedDomainCoins(): Flow<PagingData<Coin>>
}