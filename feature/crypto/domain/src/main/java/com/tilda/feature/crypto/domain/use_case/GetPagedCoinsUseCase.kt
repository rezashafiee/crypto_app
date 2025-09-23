package com.tilda.feature.crypto.domain.use_case

import androidx.paging.PagingData
import com.tilda.feature.crypto.domain.Coin
import com.tilda.feature.crypto.domain.CoinListRepository
import kotlinx.coroutines.flow.Flow

class GetPagedCoinsUseCase(
    private val repository: CoinListRepository
) {
    operator fun invoke(): Flow<PagingData<Coin>> {
        return repository.getPagedDomainCoins()
    }
}