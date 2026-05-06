package com.tilda.feature.crypto.data.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.tilda.core.data.db.dao.CoinDao
import com.tilda.core.data.db.model.CoinEntity
import com.tilda.feature.crypto.data.datasource.CoinLocalDataSource
import com.tilda.feature.crypto.data.datasource.CoinRemoteDataSource
import com.tilda.feature.crypto.data.local.CoinLocalDataSourceImp
import com.tilda.feature.crypto.data.remote.CoinApiService
import com.tilda.feature.crypto.data.paging.CoinsRemoteMediator
import com.tilda.feature.crypto.data.remote.CoinRemoteDataSourceImp
import com.tilda.feature.crypto.data.repository.CoinRepositoryImp
import com.tilda.feature.crypto.domain.interactor.GetCoinHistoryUseCase
import com.tilda.feature.crypto.domain.interactor.GetPagedCoinsUseCase
import com.tilda.feature.crypto.domain.repository.CoinRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CryptoDataModule {

    @Binds
    @Singleton
    abstract fun bindCoinRemoteDataSource(
        implementation: CoinRemoteDataSourceImp
    ): CoinRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindCoinLocalDataSource(
        implementation: CoinLocalDataSourceImp
    ): CoinLocalDataSource

    @Binds
    @Singleton
    abstract fun bindCoinRepository(
        implementation: CoinRepositoryImp
    ): CoinRepository

    companion object {
        @Provides
        @Singleton
        fun provideCoinApiService(retrofit: Retrofit): CoinApiService {
            return retrofit.create(CoinApiService::class.java)
        }

        @Provides
        @Singleton
        fun provideGetPagedCoinsUseCase(repository: CoinRepository): GetPagedCoinsUseCase {
            return GetPagedCoinsUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideGetCoinHistoryUseCase(repository: CoinRepository): GetCoinHistoryUseCase {
            return GetCoinHistoryUseCase(repository)
        }

        @Provides
        @Singleton
        fun providePagingConfig(): PagingConfig {
            return PagingConfig(pageSize = 50, initialLoadSize = 50)
        }

        @OptIn(ExperimentalPagingApi::class)
        @Provides
        @Singleton
        fun provideCoinPager(
            config: PagingConfig,
            remoteMediator: CoinsRemoteMediator,
            coinDao: CoinDao
        ): Pager<Int, CoinEntity> {
            return Pager(
                config = config,
                remoteMediator = remoteMediator,
                pagingSourceFactory = {
                    coinDao.getPagingSource()
                }
            )
        }
    }
}
