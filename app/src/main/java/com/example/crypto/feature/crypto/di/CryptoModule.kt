package com.example.crypto.feature.crypto.di

import com.example.crypto.feature.crypto.data.CoinListLocalDataSourceImp
import com.example.crypto.feature.crypto.data.CoinListRemoteDataSourceImp
import com.example.crypto.feature.crypto.data.CoinListRepositoryImp
import com.example.crypto.feature.crypto.domain.CoinListLocalDataSource
import com.example.crypto.feature.crypto.domain.CoinListRemoteDataSource
import com.example.crypto.feature.crypto.domain.CoinListRepository
import com.example.crypto.feature.crypto.presentation.coin_list.CoinListViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val cryptoModule = module {
    singleOf(::CoinListRemoteDataSourceImp) { bind<CoinListRemoteDataSource>() }
    singleOf(::CoinListLocalDataSourceImp) { bind<CoinListLocalDataSource>() }
    singleOf(::CoinListRepositoryImp) { bind<CoinListRepository>() }
    viewModelOf(::CoinListViewModel)
}