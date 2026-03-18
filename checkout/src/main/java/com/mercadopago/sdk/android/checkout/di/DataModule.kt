package com.mercadopago.sdk.android.checkout.di

import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferences
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferencesImpl
import com.mercadopago.sdk.android.checkout.data.provider.AndroidStringProvider
import com.mercadopago.sdk.android.checkout.domain.provider.StringProvider
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardDataByBinUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardIssuersUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetInstallmentsUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetPaymentMethodsUseCase
import com.mercadopago.sdk.android.checkout.presentation.factory.CardPaymentScreenStateFactory
import com.mercadopago.sdk.android.checkout.presentation.usecase.GenerateCardTokenUseCase
import com.mercadopago.sdk.android.checkout.presentation.usecase.GetIdentificationTypesUseCase
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardPaymentViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.InstallmentsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal fun provideDataModule() =
    module {
        single<CheckoutThemePreferences> {
            CheckoutThemePreferencesImpl()
        }
        single<StringProvider> {
            AndroidStringProvider(context = get())
        }
        factory {
            CardPaymentScreenStateFactory(stringProvider = get())
        }
        viewModel { (checkoutConfiguration: CheckoutConfiguration) ->
            CardPaymentViewModel(
                stateFactory = get(),
                checkoutConfiguration = checkoutConfiguration,
                getCardDataByBinUseCase = GetCardDataByBinUseCase(
                    getPaymentMethodsUseCase = GetPaymentMethodsUseCase(),
                    getCardIssuersUseCase = GetCardIssuersUseCase(),
                    getInstallmentsUseCase = GetInstallmentsUseCase(),
                ),
                getIdentificationTypesUseCase = GetIdentificationTypesUseCase(),
                generateCardTokenUseCase = GenerateCardTokenUseCase(),
            )
        }
    }

internal fun provideInstallmentsModule() =
    module {
        viewModel { InstallmentsViewModel() }
    }
