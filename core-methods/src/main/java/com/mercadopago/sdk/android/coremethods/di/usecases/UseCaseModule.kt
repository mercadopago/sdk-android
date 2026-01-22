package com.mercadopago.sdk.android.coremethods.di.usecases

import com.mercadopago.sdk.android.coremethods.domain.provider.ThreeDSProviderManager
import com.mercadopago.sdk.android.coremethods.domain.usecase.AuthenticateThreeDSChallengeUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.CloseTransactionUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.CreateTransactionUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GenerateCardIdTokenUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GenerateCardTokenUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetCardIssuersUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetIdentificationTypesUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetInstallmentsUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetPaymentMethodsUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetAuthenticationRequestParametersUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetWarningsUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.StartChallengeUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.UpdateThreeDSChallengeStatusUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.SaveThreeDSDeviceDataUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.SaveThreeDSDeviceDataOrchestratorUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun provideUseCaseModule(): Module =
    module {
        single { ThreeDSProviderManager() }
        factory { GenerateCardTokenUseCase(get()) }
        factory { GetInstallmentsUseCase(get()) }
        factory { GetIdentificationTypesUseCase(get()) }
        factory { GetCardIssuersUseCase(get()) }
        factory { GetPaymentMethodsUseCase(get()) }
        factory { GenerateCardIdTokenUseCase(get()) }
        factory { AuthenticateThreeDSChallengeUseCase(get()) }
        factory { UpdateThreeDSChallengeStatusUseCase(get()) }
        factory { GetWarningsUseCase(get()) }
        factory { StartChallengeUseCase(get(), get(), get()) }
        factory { CloseTransactionUseCase(get()) }
        factory { CreateTransactionUseCase(get()) }
        factory { GetAuthenticationRequestParametersUseCase(get()) }
        factory { SaveThreeDSDeviceDataUseCase(get()) }
        factory { SaveThreeDSDeviceDataOrchestratorUseCase(get(), get(), get(), get()) }
    }
