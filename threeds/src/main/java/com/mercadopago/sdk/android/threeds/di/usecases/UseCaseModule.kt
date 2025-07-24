package com.mercadopago.sdk.android.threeds.di.usecases

import com.mercadopago.sdk.android.threeds.domain.usecase.AuthenticateUseCase
import com.mercadopago.sdk.android.threeds.domain.usecase.RequestChallengeUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun provideUseCaseModule(): Module =
    module {
        factory { AuthenticateUseCase(get()) }
        factory { RequestChallengeUseCase(get(), get()) }
    }
