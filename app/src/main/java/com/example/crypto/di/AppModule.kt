package com.example.crypto.di

import com.example.crypto.core.data.network.HttpClientFactory
import com.example.crypto.feature.crypto.data.CoinListRemoteDataSource
import com.example.crypto.feature.crypto.domain.CoinListDataSource
import com.example.crypto.feature.crypto.presentation.coin_list.CoinListViewModel
import io.ktor.client.engine.cio.CIO
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { HttpClientFactory.create(CIO.create()) }
    single<CoinListDataSource> {
        CoinListRemoteDataSource(
            httpClient = get()
        )
    }
    viewModelOf(::CoinListViewModel)
}