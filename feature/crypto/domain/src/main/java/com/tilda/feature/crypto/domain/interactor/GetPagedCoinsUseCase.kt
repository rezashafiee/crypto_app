package com.tilda.feature.crypto.domain.interactor

import androidx.paging.PagingData
import com.tilda.feature.crypto.domain.model.Coin
import com.tilda.feature.crypto.domain.repository.CoinListRepository
import kotlinx.coroutines.flow.Flow

class GetPagedCoinsUseCase(
    private val repository: CoinListRepository
) {
    operator fun invoke(): Flow<PagingData<Coin>> {
        return repository.getPagedDomainCoins()
    }
}