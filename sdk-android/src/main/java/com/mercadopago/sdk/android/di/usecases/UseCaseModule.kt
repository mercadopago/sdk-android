package com.mercadopago.sdk.android.di.usecases

import com.mercadopago.sdk.android.domain.usecase.GetSiteIdUseCase
import com.mercadopago.sdk.android.domain.usecase.SetSiteIdUseCase
import com.mercadopago.sdk.android.initializer.usecase.ConfigureSdkUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun provideUseCaseModule(): Module = module {
    factory {
        GetSiteIdUseCase(
            sdkInitializationRepository = get(),
        )
    }
    factory {
        SetSiteIdUseCase(
            sdkInitializationRepository = get(),
        )
    }
    factory {
        ConfigureSdkUseCase(
            getSiteIdUseCase = get(),
            setSiteIdUseCase = get(),
        )
    }
}
