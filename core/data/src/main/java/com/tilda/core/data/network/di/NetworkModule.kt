package com.tilda.core.data.network.di

import com.squareup.moshi.Moshi
import com.tilda.core.data.BuildConfig
import com.tilda.core.data.network.HttpClientFactory
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModule = module {
    single { HttpClientFactory.create() }
    single { Moshi.Builder().build() }
    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .client(get())
            .build()
    }
}