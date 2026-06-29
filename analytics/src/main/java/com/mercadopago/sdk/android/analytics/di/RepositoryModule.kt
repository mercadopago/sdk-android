package com.mercadopago.sdk.android.analytics.di

import com.mercadopago.sdk.android.analytics.data.repository.AnalyticsRepositoryImpl
import com.mercadopago.sdk.android.analytics.domain.repository.AnalyticsRepository
import kotlinx.coroutines.flow.Flow
import org.koin.dsl.module

internal fun provideRepositoryModule(getSiteIdFlow: Flow<String>) = module {
    factory<AnalyticsRepository> {
        AnalyticsRepositoryImpl(
            localDataSource = get(),
            remoteDataSource = get(),
            getSiteIdFlow = getSiteIdFlow,
        )
    }
}
