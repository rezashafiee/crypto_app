package com.tilda.feature.crypto.domain.repository

import androidx.paging.PagingData
import com.tilda.feature.crypto.domain.model.Coin
import kotlinx.coroutines.flow.Flow

interface CoinListRepository {
    fun getPagedDomainCoins(): Flow<PagingData<Coin>>
}