package com.mercadopago.sdk.android.analytics.di

import com.mercadopago.sdk.android.analytics.domain.usecase.TrackMetricUseCase
import org.koin.dsl.module

internal fun provideUseCaseModule() = module {
    factory { TrackMetricUseCase(analyticsRepository = get()) }
}
