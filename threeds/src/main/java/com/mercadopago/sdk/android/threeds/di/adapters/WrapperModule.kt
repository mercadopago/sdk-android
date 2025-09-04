package com.mercadopago.sdk.android.threeds.di.adapters

import android.content.Context
import com.mercadopago.sdk.android.threeds.data.adapter.ThreeDSWrapperImpl
import com.mercadopago.sdk.android.threeds.domain.adapter.ThreeDSWrapper
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun provideWrapperModule(context: Context): Module =
    module {
        single<ThreeDSWrapper> {
            ThreeDSWrapperImpl(context)
        }
    }
