package com.example.crypto.di

import com.example.crypto.core.data.network.HttpClientFactory
import com.example.crypto.feature.crypto.data.CoinListRemoteDataSourceImp
import com.example.crypto.feature.crypto.presentation.coin_list.CoinListViewModel
import io.ktor.client.engine.cio.CIO
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { HttpClientFactory.create(CIO.create()) }
    single<com.example.crypto.feature.crypto.domain.CoinListRemoteDataSource> {
        CoinListRemoteDataSourceImp(
            httpClient = get()
        )
    }
    viewModelOf(::CoinListViewModel)
}