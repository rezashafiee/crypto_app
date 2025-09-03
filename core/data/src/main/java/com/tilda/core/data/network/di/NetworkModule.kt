package com.tilda.core.data.network.di

import io.ktor.client.engine.cio.CIO
import com.tilda.core.data.network.HttpClientFactory
import org.koin.dsl.module

val networkModule = module {
    single { HttpClientFactory.create(CIO.create()) }
}