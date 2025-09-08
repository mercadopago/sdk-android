package com.mercadopago.sdk.android.threeds.di.adapters

import android.content.Context
import com.mercadopago.sdk.android.threeds.data.wrapper.ThreeDSWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun provideWrapperModule(context: Context): Module =
    module {
        single<ThreeDSWrapper> {
            val wrapper = ThreeDSWrapper(context)

            // Initialize wrapper automatically when created by Koin
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    wrapper.initialize()
                } catch (e: Exception) {
                    // Log error but don't fail the creation
                    // The wrapper can be retried later if needed
                }
            }

            wrapper
        }
    }
