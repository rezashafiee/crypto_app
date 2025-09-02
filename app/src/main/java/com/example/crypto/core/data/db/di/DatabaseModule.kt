package com.example.crypto.core.data.db.di

import androidx.room.Room
import com.example.crypto.core.data.db.CoinDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            CoinDatabase::class.java,
            "coin_database"
        ).build()
    }
    single { get<CoinDatabase>().coinDao() }
}