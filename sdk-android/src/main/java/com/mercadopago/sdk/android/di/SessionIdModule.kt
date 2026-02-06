package com.mercadopago.sdk.android.di

import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun provideSessionIdModule(): Module = module {
    single<SessionIdProvider> {
        object : SessionIdProvider {
            override fun getSessionId(): String = MercadoPagoSDK.getInstance().sessionId
        }
    }
}
