package com.mercadopago.sdk.android.checkout.di

import com.mercadopago.sdk.android.checkout.data.remote.service.CardFormService
import com.mercadopago.sdk.android.checkout.domain.usecase.CardFormInitUseCase
import com.mercadopago.sdk.android.core.di.RetrofitServiceFactory
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import org.koin.dsl.module

internal fun provideNetworkModule() =
    module {
        single { get<RetrofitServiceFactory>().createService(CardFormService::class.java) }
        factory {
            CardFormInitUseCase(
                countryCode = MercadoPagoSDK.countryCode,
                cardFormService = get(),
            )
        }
    }
