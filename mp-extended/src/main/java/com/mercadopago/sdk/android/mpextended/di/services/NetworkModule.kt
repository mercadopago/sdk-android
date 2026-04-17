package com.mercadopago.sdk.android.mpextended.di.services

import com.mercadopago.sdk.android.core.di.RetrofitServiceFactory
import com.mercadopago.sdk.android.mpextended.data.remote.service.MPExtendedService
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun provideNetworkModule(): Module =
    module {
        single { get<RetrofitServiceFactory>().createService(MPExtendedService::class.java) }
    }
