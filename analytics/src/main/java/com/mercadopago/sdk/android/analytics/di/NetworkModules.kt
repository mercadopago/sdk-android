package com.mercadopago.sdk.android.analytics.di

import com.mercadopago.sdk.android.analytics.data.remote.service.AnalyticsService
import com.mercadopago.sdk.android.core.BuildConfig
import com.mercadopago.sdk.android.core.di.RetrofitServiceFactory
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val ANALYTICS_SERVICE_FACTORY = "analytics_service_factory"

internal fun provideNetworkModule() = module {
    single {
        get<RetrofitServiceFactory>(
            named(ANALYTICS_SERVICE_FACTORY)
        ).createService(AnalyticsService::class.java)
    }
    single(named(ANALYTICS_SERVICE_FACTORY)) {
        RetrofitServiceFactory(
            publicKey = null,
            baseUrl = BuildConfig.MERCADO_LIBRE_API_URL,
        )
    }
}
