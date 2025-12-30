package com.mercadopago.sdk.android.coremethods.di.usecases

import com.mercadopago.sdk.android.coremethods.domain.usecase.AuthenticateThreeDSChallengeUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GenerateCardIdTokenUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GenerateCardTokenUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetCardIssuersUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetIdentificationTypesUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetInstallmentsUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetPaymentMethodsUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun provideUseCaseModule(): Module =
    module {
        factory { GenerateCardTokenUseCase(get()) }
        factory { GetInstallmentsUseCase(get()) }
        factory { GetIdentificationTypesUseCase(get()) }
        factory { GetCardIssuersUseCase(get()) }
        factory { GetPaymentMethodsUseCase(get()) }
        factory { GenerateCardIdTokenUseCase(get()) }
        factory { AuthenticateThreeDSChallengeUseCase(get()) }
    }
