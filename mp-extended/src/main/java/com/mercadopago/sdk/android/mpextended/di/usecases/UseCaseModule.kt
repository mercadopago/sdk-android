package com.mercadopago.sdk.android.mpextended.di.usecases

import com.mercadopago.sdk.android.mpextended.domain.usecase.GetDeviceSessionUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun provideUseCaseModule(): Module =
    module {
        factory { GetDeviceSessionUseCase(get()) }
    }
