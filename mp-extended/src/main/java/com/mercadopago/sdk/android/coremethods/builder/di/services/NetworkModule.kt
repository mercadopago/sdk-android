package com.mercadopago.sdk.android.coremethods.builder.di.services

import com.mercadopago.sdk.android.core.di.RetrofitServiceFactory
import com.mercadopago.sdk.android.coremethods.builder.data.remote.service.MPExtendedService
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun provideNetworkModule(
    baseUrl: String,
): Module =
    module {
        single { RetrofitServiceFactory(baseUrl = baseUrl, publicKey = null) }
        single {
            get<RetrofitServiceFactory>().createService(MPExtendedService::class.java)
        }
    }
