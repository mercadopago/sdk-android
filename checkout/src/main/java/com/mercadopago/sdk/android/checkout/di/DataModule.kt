package com.mercadopago.sdk.android.checkout.di

import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferences
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferencesImpl
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardDataByBinUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardIssuersUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetInstallmentsUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetPaymentMethodsUseCase
import com.mercadopago.sdk.android.checkout.presentation.usecase.GetIdentificationTypesUseCase
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardPaymentViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.InstallmentsViewModel
import com.mercadopago.sdk.android.coremethods.domain.interactor.coreMethods
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
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
                    getPaymentMethodsUseCase = GetPaymentMethodsUseCase(MercadoPagoSDK.getInstance().coreMethods),
                    getCardIssuersUseCase = GetCardIssuersUseCase(MercadoPagoSDK.getInstance().coreMethods),
                    getInstallmentsUseCase = GetInstallmentsUseCase(MercadoPagoSDK.getInstance().coreMethods),
                ),
                getIdentificationTypesUseCase = GetIdentificationTypesUseCase(MercadoPagoSDK.getInstance().coreMethods),
            )
        }
    }

internal fun provideInstallmentsModule() =
    module {
        viewModel { InstallmentsViewModel() }
    }
