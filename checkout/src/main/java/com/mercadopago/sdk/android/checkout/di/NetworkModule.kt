package com.mercadopago.sdk.android.checkout.di

import com.mercadopago.sdk.android.checkout.data.remote.service.CardFormService
import com.mercadopago.sdk.android.core.di.RetrofitServiceFactory
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun provideNetworkModule(
    publicKey: String,
    baseUrl: String,
): Module =
    module {
        single { RetrofitServiceFactory(publicKey, baseUrl) }
        single {
            get<RetrofitServiceFactory>().createService(CardFormService::class.java)
        }
    }
