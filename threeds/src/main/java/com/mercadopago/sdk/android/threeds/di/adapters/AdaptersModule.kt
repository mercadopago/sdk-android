package com.mercadopago.sdk.android.threeds.di.adapters

import android.content.Context
import com.mercadopago.sdk.android.threeds.data.adapter.USDKThreeDSAdapter
import com.mercadopago.sdk.android.threeds.domain.adapter.ThreeDSSDKAdapter
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun provideAdaptersModule(context: Context): Module =
    module {
        factory<ThreeDSSDKAdapter> {
            USDKThreeDSAdapter(context)
        }
    }
