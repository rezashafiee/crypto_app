package com.tilda.core.data.network

import com.tilda.core.data.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

object HttpClientFactory {

    fun create(): OkHttpClient {
        val loggingInterceptor =
            HttpLoggingInterceptor { message ->
                Timber.tag("OkHttp").d(message)
            }.apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }
}
