package com.mercadopago.sdk.android.coremethods.di.usecases

import com.mercadopago.sdk.android.coremethods.domain.usecase.GenerateCardTokenUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun provideUseCaseModule(): Module = module {
    factory { GenerateCardTokenUseCase(get()) }
}
