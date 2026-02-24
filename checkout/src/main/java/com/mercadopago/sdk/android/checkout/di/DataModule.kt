package com.mercadopago.sdk.android.checkout.di

import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferences
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferencesImpl
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardDataByBinUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardIssuersByBinUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetInstallmentsByBinUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetPaymentMethodsByBinUseCase
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardPaymentViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.InstallmentsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal fun provideDataModule() =
    module {
        single<CheckoutThemePreferences> {
            CheckoutThemePreferencesImpl()
        }
        viewModel { (checkoutConfiguration: CheckoutConfiguration) ->
            CardPaymentViewModel(
                checkoutConfiguration = checkoutConfiguration,
                getCardDataByBinUseCase = GetCardDataByBinUseCase(
                    getPaymentMethodsUseCase = GetPaymentMethodsByBinUseCase(),
                    getCardIssuersUseCase = GetCardIssuersByBinUseCase(),
                    getInstallmentsUseCase = GetInstallmentsByBinUseCase(),
                ),
            )
        }
    }

internal fun provideInstallmentsModule() =
    module {
        viewModel { InstallmentsViewModel() }
    }
