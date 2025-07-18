package com.example.crypto

import android.app.Application
import com.example.crypto.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin

class CoinTrackerApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModule)
            androidContext(this@CoinTrackerApp)
            logger(AndroidLogger())
        }
    }
}