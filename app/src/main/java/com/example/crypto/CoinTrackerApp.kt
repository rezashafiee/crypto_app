package com.example.crypto

import android.app.Application
import com.example.crypto.core.data.db.di.databaseModule
import com.example.crypto.core.data.network.di.networkModule
import com.example.crypto.di.appModule
import com.example.crypto.feature.crypto.di.cryptoModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class CoinTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                appModule,
                databaseModule,
                networkModule,
                cryptoModule
            )
            androidContext(this@CoinTrackerApp)
            workManagerFactory()
            logger(AndroidLogger())
        }
    }
}