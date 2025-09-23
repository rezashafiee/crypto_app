package com.tilda.feature.crypto.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.tilda.core.data.db.model.CoinEntity
import com.tilda.feature.crypto.data.mapper.toCoin
import com.tilda.feature.crypto.domain.model.Coin
import com.tilda.feature.crypto.domain.repository.CoinListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CoinListRepositoryImp(
    private val pager: Pager<Int, CoinEntity>
) : CoinListRepository {

    override fun getPagedDomainCoins(): Flow<PagingData<Coin>> {
        return pager.flow.map {
            pagingData -> pagingData.map { coinEntity -> coinEntity.toCoin() }
        }
    }
}