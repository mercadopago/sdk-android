package com.mercadopago.sdk.android.checkout.di

import com.mercadopago.android.sdk.checkout.BuildConfig
import com.mercadopago.sdk.android.checkout.data.remote.service.CardFormService
import com.mercadopago.sdk.android.core.di.RetrofitServiceFactory
import com.mercadopago.sdk.android.core.utils.FuryTokenStore
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import org.koin.dsl.module

internal fun provideNetworkModule() =
    module {
        single<CardFormService> {
            FuryTokenStore.token = BuildConfig.FURY_TOKEN
            RetrofitServiceFactory(
                publicKey = MercadoPagoSDK.publicKey,
                baseUrl = BuildConfig.CHECKOUT_BFF_BASE_URL,
            ).createService(CardFormService::class.java)
        }
    }
