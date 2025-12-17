package com.mercadopago.sdk.android.threeds.di.repository

import com.mercadopago.sdk.android.threeds.data.repository.ThreeDSRepositoryImpl
import com.mercadopago.sdk.android.threeds.domain.repository.ThreeDSRepository
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun provideRepositoryModule(): Module =
    module {
        single<ThreeDSRepository> {
            ThreeDSRepositoryImpl(
                threeDSWrapper = get(),
            )
        }
    }
