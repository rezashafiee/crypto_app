package com.tilda.feature.crypto.data.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.RemoteMediator
import com.tilda.core.data.db.dao.CoinDao
import com.tilda.core.data.db.model.CoinEntity
import com.tilda.feature.crypto.data.datasource.CoinListLocalDataSource
import com.tilda.feature.crypto.data.datasource.CoinListRemoteDataSource
import com.tilda.feature.crypto.data.local.CoinListLocalDataSourceImp
import com.tilda.feature.crypto.data.remote.CoinListRemoteDataSourceImp
import com.tilda.feature.crypto.data.repository.CoinListRepositoryImp
import com.tilda.feature.crypto.data.paging.CoinsRemoteMediator
import com.tilda.feature.crypto.domain.interactor.GetPagedCoinsUseCase
import com.tilda.feature.crypto.domain.repository.CoinListRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

@OptIn(ExperimentalPagingApi::class)
val cryptoDataModule = module {
    singleOf(::CoinListRemoteDataSourceImp) { bind<CoinListRemoteDataSource>() }
    singleOf(::CoinListLocalDataSourceImp) { bind<CoinListLocalDataSource>() }
    singleOf(::CoinListRepositoryImp) { bind<CoinListRepository>() }
    singleOf(::CoinsRemoteMediator) { bind<RemoteMediator<Int, CoinEntity>>() }
    singleOf(::GetPagedCoinsUseCase)
    single { PagingConfig(pageSize = 50, initialLoadSize = 50) }
    factory {
        Pager(
            config = get(),
            remoteMediator = get<RemoteMediator<Int, CoinEntity>>(),
            pagingSourceFactory = {
                get<CoinDao>().getPagingSource()
            }
        )
    }
}