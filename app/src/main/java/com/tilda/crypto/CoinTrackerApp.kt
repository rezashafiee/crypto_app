package com.tilda.crypto

import android.app.Application
import com.tilda.core.data.db.di.databaseModule
import com.tilda.core.data.network.di.networkModule
import com.tilda.feature.crypto.data.di.cryptoDataModule
import com.tilda.feature.crypto.presentation.di.cryptoPresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class CoinTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                databaseModule,
                networkModule,
                cryptoDataModule,
                cryptoPresentationModule
            )
            androidContext(this@CoinTrackerApp)
            workManagerFactory()
            logger(AndroidLogger())
        }
    }
}