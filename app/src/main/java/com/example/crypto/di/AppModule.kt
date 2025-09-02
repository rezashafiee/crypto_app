package com.example.crypto.di

import androidx.room.Room
import com.example.crypto.core.data.db.CoinDatabase
import com.example.crypto.core.data.network.HttpClientFactory
import com.example.crypto.core.domain.util.RemoteSyncResult
import com.example.crypto.feature.crypto.data.CoinListLocalDataSourceImp
import com.example.crypto.feature.crypto.data.CoinListRemoteDataSourceImp
import com.example.crypto.feature.crypto.data.CoinListRepositoryImp
import com.example.crypto.feature.crypto.domain.CoinListLocalDataSource
import com.example.crypto.feature.crypto.domain.CoinListRemoteDataSource
import com.example.crypto.feature.crypto.domain.CoinListRepository
import com.example.crypto.feature.crypto.presentation.coin_list.CoinListViewModel
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { HttpClientFactory.create(CIO.create()) }
    single { MutableSharedFlow<RemoteSyncResult>() }
    single { get<MutableSharedFlow<RemoteSyncResult>>().asSharedFlow() }

    singleOf(::CoinListRemoteDataSourceImp) { bind<CoinListRemoteDataSource>() }
    singleOf(::CoinListLocalDataSourceImp) { bind<CoinListLocalDataSource>() }
    singleOf(::CoinListRepositoryImp) { bind<CoinListRepository>() }

    single {
        Room.databaseBuilder(
            androidContext(),
            CoinDatabase::class.java,
            "coin_database"
        ).build()
    }
    single { get<CoinDatabase>().coinDao() }
    viewModelOf(::CoinListViewModel)
}