package com.mercadopago.sdk.android.di.repository

import com.mercadopago.sdk.android.data.repository.SdkInitializationRepositoryImpl
import com.mercadopago.sdk.android.domain.repository.SdkInitializationRepository
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun provideRepositoryModule(): Module = module {
    factory<SdkInitializationRepository> {
        SdkInitializationRepositoryImpl(
            sdkInitializationRemoteDataSource = get(),
            sdkInitializationLocalDataSource = get(),
        )
    }
}
