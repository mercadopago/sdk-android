package com.mercadopago.sdk.android.checkout.di

import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferences
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferencesImpl
import com.mercadopago.sdk.android.checkout.data.provider.AndroidStringProvider
import com.mercadopago.sdk.android.checkout.data.remote.datasource.CardFormRemoteDataSource
import com.mercadopago.sdk.android.checkout.data.remote.datasource.CardFormRemoteDataSourceImpl
import com.mercadopago.sdk.android.checkout.domain.provider.StringProvider
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardBinUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardDataByBinUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardIssuersUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetInstallmentsUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetPaymentMethodsUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.InitializeCardFormUseCase
import com.mercadopago.sdk.android.checkout.presentation.factory.CardPaymentScreenStateFactory
import com.mercadopago.sdk.android.checkout.presentation.usecase.CancelledFormContextUseCase
import com.mercadopago.sdk.android.checkout.presentation.usecase.GenerateTokenUseCase
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
        factory<CardFormRemoteDataSource> {
            CardFormRemoteDataSourceImpl(service = get())
        }
        factory {
            InitializeCardFormUseCase(cardFormRemoteDataSource = get())
        }
        factory {
            CardPaymentScreenStateFactory(stringProvider = get())
        }
        viewModel { (checkoutConfiguration: CheckoutConfiguration) ->
            CardPaymentViewModel(
                stateFactory = get(),
                checkoutConfiguration = checkoutConfiguration,
                getCardBinUseCase = GetCardBinUseCase(cardFormRemoteDataSource = get()),
                getCardDataByBinUseCase = GetCardDataByBinUseCase(
                    getPaymentMethodsUseCase = GetPaymentMethodsUseCase(),
                    getCardIssuersUseCase = GetCardIssuersUseCase(),
                    getInstallmentsUseCase = GetInstallmentsUseCase(),
                    stringProvider = get(),
                ),
                initializeCardFormUseCase = get(),
                generateTokenUseCase = GenerateTokenUseCase(),
                cancelledFormContextUseCase = CancelledFormContextUseCase(),
            )
        }
    }

internal fun provideInstallmentsModule() =
    module {
        viewModel { InstallmentsViewModel() }
    }
