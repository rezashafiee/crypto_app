package com.tilda.feature.crypto.data.di

import com.tilda.feature.crypto.data.CoinListLocalDataSourceImp
import com.tilda.feature.crypto.data.CoinListRemoteDataSourceImp
import com.tilda.feature.crypto.data.CoinListRepositoryImp
import com.tilda.feature.crypto.domain.CoinListLocalDataSource
import com.tilda.feature.crypto.domain.CoinListRemoteDataSource
import com.tilda.feature.crypto.domain.CoinListRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val cryptoDataModule = module {
    singleOf(::CoinListRemoteDataSourceImp) { bind<CoinListRemoteDataSource>() }
    singleOf(::CoinListLocalDataSourceImp) { bind<CoinListLocalDataSource>() }
    singleOf(::CoinListRepositoryImp) { bind<CoinListRepository>() }
}