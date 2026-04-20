package com.mercadopago.sdk.android.checkout.di

import com.mercadopago.sdk.android.checkout.data.remote.service.CardFormService
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun provideNetworkModule(
    baseUrl: String,
): Module =
    module {
        single { RetrofitFactory(baseUrl) }
        single {
            get<RetrofitFactory>().createService(CardFormService::class.java)
        }
    }
