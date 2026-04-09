package com.mercadopago.sdk.android.checkout.di

import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferences
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferencesImpl
import com.mercadopago.sdk.android.checkout.data.provider.AndroidStringProvider
import com.mercadopago.sdk.android.checkout.domain.provider.StringProvider
import com.mercadopago.sdk.android.checkout.domain.usecase.CardFormInitUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardDataByBinUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardIssuersUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetInstallmentsUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetPaymentMethodsUseCase
import com.mercadopago.sdk.android.checkout.presentation.factory.CardPaymentScreenStateFactory
import com.mercadopago.sdk.android.checkout.presentation.usecase.CancelledFormContextUseCase
import com.mercadopago.sdk.android.checkout.presentation.usecase.GenerateTokenUseCase
import com.mercadopago.sdk.android.checkout.presentation.usecase.GetIdentificationTypesUseCase
import com.mercadopago.sdk.android.checkout.presentation.validation.CardPaymentValidator
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardPaymentViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.InstallmentsViewModel
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal fun provideDataModule() =
    module {
        single<CheckoutThemePreferences> {
            CheckoutThemePreferencesImpl()
        }
        single<StringProvider> {
            AndroidStringProvider(
                context = get(),
                countryCode = MercadoPagoSDK.countryCode,
            )
        }
        factory {
            CardPaymentScreenStateFactory(stringProvider = get())
        }
        factory {
            CardPaymentValidator(stringProvider = get())
        }
        factory {
            CardFormInitUseCase(
                countryCode = MercadoPagoSDK.countryCode,
                cardFormService = get(),
            )
        }
        viewModel { (checkoutConfiguration: CheckoutConfiguration) ->
            CardPaymentViewModel(
                stateFactory = get(),
                checkoutConfiguration = checkoutConfiguration,
                cardFormInitUseCase = get(),
                getCardDataByBinUseCase = GetCardDataByBinUseCase(
                    getPaymentMethodsUseCase = GetPaymentMethodsUseCase(),
                    getCardIssuersUseCase = GetCardIssuersUseCase(),
                    getInstallmentsUseCase = GetInstallmentsUseCase(),
                    stringProvider = get(),
                ),
                getIdentificationTypesUseCase = GetIdentificationTypesUseCase(),
                generateTokenUseCase = GenerateTokenUseCase(),
                cancelledFormContextUseCase = CancelledFormContextUseCase(),
                validator = get(),
            )
        }
    }

internal fun provideInstallmentsModule() =
    module {
        viewModel { InstallmentsViewModel() }
    }
