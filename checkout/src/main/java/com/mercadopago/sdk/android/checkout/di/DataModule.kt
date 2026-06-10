package com.mercadopago.sdk.android.checkout.di

import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferences
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferencesImpl
import com.mercadopago.sdk.android.checkout.data.provider.AndroidStringProvider
import com.mercadopago.sdk.android.checkout.data.remote.datasource.CardFormRemoteDataSource
import com.mercadopago.sdk.android.checkout.data.remote.datasource.CardFormRemoteDataSourceImpl
import com.mercadopago.sdk.android.checkout.data.repository.CardFormRepositoryImpl
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.provider.StringProvider
import com.mercadopago.sdk.android.checkout.domain.repository.CardFormRepository
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardBinUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.InitializeCardFormUseCase
import com.mercadopago.sdk.android.checkout.presentation.factory.CardPaymentScreenStateFactory
import com.mercadopago.sdk.android.checkout.presentation.usecase.GenerateTokenUseCase
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardPaymentViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.InstallmentsViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.PaymentBrickViewModel
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
        factory<CardFormRepository> {
            CardFormRepositoryImpl(dataSource = get())
        }
        factory {
            InitializeCardFormUseCase(repository = get())
        }
        factory {
            CardPaymentScreenStateFactory(stringProvider = get())
        }
        viewModel { (checkoutConfiguration: CheckoutConfiguration?) ->
            CardPaymentViewModel(
                checkoutConfiguration = checkoutConfiguration,
                getCardBinUseCase = GetCardBinUseCase(repository = get()),
                initializeCardFormUseCase = get(),
                generateTokenUseCase = GenerateTokenUseCase(),
                cardPaymentScreenStateFactory = get(),
            )
        }
        viewModel { (installmentData: MPInstallmentData, paymentData: MPPaymentData, checkoutType: String) ->
            InstallmentsViewModel(
                installmentData = installmentData,
                paymentData = paymentData,
                checkoutType = checkoutType,
            )
        }
        viewModel { PaymentBrickViewModel() }
    }
