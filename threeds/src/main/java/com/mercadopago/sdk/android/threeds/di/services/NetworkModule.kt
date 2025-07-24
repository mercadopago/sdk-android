package com.mercadopago.sdk.android.threeds.di.services

import com.mercadopago.sdk.android.core.di.RetrofitServiceFactory
import com.mercadopago.sdk.android.threeds.data.remote.service.ThreeDSService
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun provideNetworkModule(): Module =
    module {
        single { get<RetrofitServiceFactory>().createService(ThreeDSService::class.java) }
    }
