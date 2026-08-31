package com.mercadopago.sdk.android.checkout.di

import com.mercadopago.sdk.android.checkout.data.remote.service.CardFormService
import com.mercadopago.sdk.android.checkout.data.remote.service.OrderService
import com.mercadopago.sdk.android.checkout.data.remote.service.PaymentBrickInitializationService
import com.mercadopago.sdk.android.checkout.data.remote.service.ReviewConfirmService
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun provideNetworkModule(
    baseUrl: String,
): Module =
    module {
        single { RetrofitFactory(baseUrl) }
        single {
            get<RetrofitFactory>().createService(CardFormService::class.java)
        }
        single {
            get<RetrofitFactory>().createService(OrderService::class.java)
        }
        single {
            get<RetrofitFactory>().createService(PaymentBrickInitializationService::class.java)
        }
        single {
            get<RetrofitFactory>().createService(ReviewConfirmService::class.java)
        }
    }
