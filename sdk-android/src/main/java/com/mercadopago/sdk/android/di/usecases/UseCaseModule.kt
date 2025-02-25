package com.mercadopago.sdk.android.di.usecases

import com.mercadopago.sdk.android.domain.usecase.FetchSiteIdUseCase
import com.mercadopago.sdk.android.domain.usecase.GetSiteIdUseCase
import com.mercadopago.sdk.android.domain.usecase.SetSiteIdUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun provideUseCaseModule(): Module = module {
    factory {
        FetchSiteIdUseCase(
            sdkInitializationRepository = get(),
        )
    }
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
}
