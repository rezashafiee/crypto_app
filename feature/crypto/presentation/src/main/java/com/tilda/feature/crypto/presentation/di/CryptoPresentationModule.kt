package com.tilda.feature.crypto.presentation.di

import com.tilda.feature.crypto.domain.use_case.GetPagedCoinsUseCase
import com.tilda.feature.crypto.presentation.coin_list.CoinListViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val cryptoPresentationModule = module {
    singleOf(::GetPagedCoinsUseCase)
    viewModelOf(::CoinListViewModel)
}