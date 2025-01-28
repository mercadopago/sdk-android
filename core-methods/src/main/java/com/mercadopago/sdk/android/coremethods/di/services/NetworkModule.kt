package com.mercadopago.sdk.android.coremethods.di.services

import com.mercadopago.sdk.android.core.di.RetrofitServiceFactory
import com.mercadopago.sdk.android.coremethods.data.remote.service.CoreMethodsService
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun provideNetworkModule(publicKey: String): Module = module {
    single { RetrofitServiceFactory(publicKey) }
    single { get<RetrofitServiceFactory>().createService(CoreMethodsService::class.java) }
}
