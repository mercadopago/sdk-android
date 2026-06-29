package com.mercadopago.sdk.android.di.services

import com.mercadopago.sdk.android.core.di.RetrofitServiceFactory
import com.mercadopago.sdk.android.data.remote.service.SdkInitializationService
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun provideNetworkModule(
    publicKey: String,
    baseUrl: String,
): Module = module {
    single { RetrofitServiceFactory(publicKey, baseUrl) }
    single {
        get<RetrofitServiceFactory>().createService(SdkInitializationService::class.java)
    }
}
