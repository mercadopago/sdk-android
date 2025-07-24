package com.mercadopago.sdk.android.threeds.di.adapters

import com.mercadopago.sdk.android.threeds.data.adapter.DefaultThreeDSSDKAdapter
import com.mercadopago.sdk.android.threeds.domain.adapter.ThreeDSSDKAdapter
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun provideAdaptersModule(): Module =
    module {
        factory<ThreeDSSDKAdapter> {
            DefaultThreeDSSDKAdapter()
        }
    }
