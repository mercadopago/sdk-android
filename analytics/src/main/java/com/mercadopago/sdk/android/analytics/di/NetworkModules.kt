package com.mercadopago.sdk.android.analytics.di

import com.mercadopago.sdk.android.analytics.data.remote.service.AnalyticsService
import com.mercadopago.sdk.android.core.di.RetrofitServiceFactory
import com.mercadopago.sdk.android.core.utils.isDebugApp
import com.mercadopago.sdk.android.core.utils.isSameLibraryGroup
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
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
            baseUrl = "https://api.mercadolibre.com/",
            logLevel = if (isDebugApp() && isSameLibraryGroup(androidContext())) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        )
    }
}
