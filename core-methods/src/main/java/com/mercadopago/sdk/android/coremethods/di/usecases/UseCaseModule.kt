package com.mercadopago.sdk.android.coremethods.di.usecases

import com.mercadopago.sdk.android.coremethods.di.SecurityCodeLengthProvider
import com.mercadopago.sdk.android.coremethods.di.SecurityCodeLengthProviderImpl
import com.mercadopago.sdk.android.coremethods.domain.usecase.GenerateCardIdTokenUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GenerateCardTokenUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetCardIssuersUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetIdentificationTypesUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetInstallmentsUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetPaymentMethodsUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.validations.IsSecurityCodeValidUseCase
import com.mercadopago.sdk.android.di.SessionIdProvider
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

internal fun provideUseCaseModule(): Module =
    module {
        single { SecurityCodeLengthProviderImpl() } bind SecurityCodeLengthProvider::class
        factory { GetInstallmentsUseCase(get()) }
        factory { GetIdentificationTypesUseCase(get()) }
        factory { GetCardIssuersUseCase(get()) }
        factory { GetPaymentMethodsUseCase(get()) }
        factory { IsSecurityCodeValidUseCase() }
        factory { GenerateCardTokenUseCase(get(), get(), get<SessionIdProvider>(), get()) }
        factory {
            GenerateCardIdTokenUseCase(
                get(),
                get<SessionIdProvider>(),
                get<SecurityCodeLengthProvider>(),
                get()
            )
        }
    }
