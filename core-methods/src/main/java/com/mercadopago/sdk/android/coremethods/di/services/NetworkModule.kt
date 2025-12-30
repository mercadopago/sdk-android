package com.mercadopago.sdk.android.coremethods.di.services

import com.mercadopago.sdk.android.core.di.RetrofitServiceFactory
import com.mercadopago.sdk.android.coremethods.data.remote.service.CoreMethodsService
import com.mercadopago.sdk.android.coremethods.data.remote.service.ThreeDSService
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun provideNetworkModule(): Module =
    module {
        single { get<RetrofitServiceFactory>().createService(CoreMethodsService::class.java) }
        single { get<RetrofitServiceFactory>().createService(ThreeDSService::class.java) }
    }
