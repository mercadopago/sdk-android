package com.mercadopago.sdk.android.threeds.di.adapters

import android.content.Context
import com.mercadopago.sdk.android.threeds.data.wrapper.ThreeDSWrapper
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun provideWrapperModule(context: Context): Module =
    module {
        single<ThreeDSWrapper> {
            ThreeDSWrapper(context)
        }
    }
