package com.example.crypto.di

import com.example.crypto.core.domain.util.RemoteSyncResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.koin.dsl.module

val appModule = module {
    single { MutableSharedFlow<RemoteSyncResult>() }
    single { get<MutableSharedFlow<RemoteSyncResult>>().asSharedFlow() }
}