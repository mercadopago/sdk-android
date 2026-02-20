package com.mercadopago.sdk.android.checkout.di

import com.mercadopago.sdk.android.checkout.core.model.internal.Configuration
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferences
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferencesImpl
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardPaymentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal fun provideDataModule() =
    module {
        single<CheckoutThemePreferences> {
            CheckoutThemePreferencesImpl()
        }
        viewModel { (configuration: Configuration) ->
            CardPaymentViewModel(configuration = configuration)
        }
    }
