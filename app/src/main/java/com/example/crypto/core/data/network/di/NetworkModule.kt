package com.example.crypto.core.data.network.di

import com.example.crypto.core.data.network.HttpClientFactory
import io.ktor.client.engine.cio.CIO
import org.koin.dsl.module

val networkModule = module {
    single { HttpClientFactory.create(CIO.create()) }
}