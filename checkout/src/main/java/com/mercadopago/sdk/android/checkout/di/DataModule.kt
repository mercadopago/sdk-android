package com.mercadopago.sdk.android.checkout.di

import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.core.model.internal.getSellerInfo
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferences
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferencesImpl
import com.mercadopago.sdk.android.checkout.data.provider.AndroidStringProvider
import com.mercadopago.sdk.android.checkout.data.remote.datasource.CardFormRemoteDataSource
import com.mercadopago.sdk.android.checkout.data.remote.datasource.CardFormRemoteDataSourceImpl
import com.mercadopago.sdk.android.checkout.data.remote.datasource.OrderRemoteDataSource
import com.mercadopago.sdk.android.checkout.data.remote.datasource.OrderRemoteDataSourceImpl
import com.mercadopago.sdk.android.checkout.data.remote.datasource.PaymentBrickInitializationRemoteDataSource
import com.mercadopago.sdk.android.checkout.data.remote.datasource.PaymentBrickInitializationRemoteDataSourceImpl
import com.mercadopago.sdk.android.checkout.data.remote.datasource.ReviewConfirmRemoteDataSource
import com.mercadopago.sdk.android.checkout.data.remote.datasource.ReviewConfirmRemoteDataSourceImpl
import com.mercadopago.sdk.android.checkout.data.repository.CardFormRepositoryImpl
import com.mercadopago.sdk.android.checkout.data.repository.OrderRepositoryImpl
import com.mercadopago.sdk.android.checkout.data.repository.PaymentBrickInitializationRepositoryImpl
import com.mercadopago.sdk.android.checkout.data.repository.ReviewConfirmRepositoryImpl
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionScreenData
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.checkout.domain.provider.StringProvider
import com.mercadopago.sdk.android.checkout.domain.repository.CardFormRepository
import com.mercadopago.sdk.android.checkout.domain.repository.OrderRepository
import com.mercadopago.sdk.android.checkout.domain.repository.PaymentBrickInitializationRepository
import com.mercadopago.sdk.android.checkout.domain.usecase.FetchMethodSelectionScreenUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.FetchPaymentBrickInitializationUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.FetchReviewConfirmUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GenerateTokenWithCardIdUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardBinUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetSecurityCodeScreenUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.InitializeCardFormUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.ProcessOrderUseCase
import com.mercadopago.sdk.android.checkout.presentation.factory.CardPaymentScreenStateFactory
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeScreenConfig
import com.mercadopago.sdk.android.checkout.presentation.usecase.CancelledPaymentContextUseCase
import com.mercadopago.sdk.android.checkout.presentation.usecase.GenerateTokenUseCase
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardPaymentViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.InstallmentsViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.MethodSelectionViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.PaymentBrickViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.ReviewConfirmViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.SecurityCodeViewModel
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@Suppress("LongMethod")
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
        single {
            CancelledPaymentContextUseCase()
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
        factory<OrderRemoteDataSource> {
            OrderRemoteDataSourceImpl(service = get())
        }
        factory<OrderRepository> {
            OrderRepositoryImpl(dataSource = get())
        }
        factory<PaymentBrickInitializationRemoteDataSource> {
            PaymentBrickInitializationRemoteDataSourceImpl(service = get())
        }
        factory<PaymentBrickInitializationRepository> {
            PaymentBrickInitializationRepositoryImpl(dataSource = get())
        }
        factory {
            FetchPaymentBrickInitializationUseCase(repository = get())
        }
        factory {
            GetSecurityCodeScreenUseCase()
        }
        factory {
            FetchMethodSelectionScreenUseCase()
        }
        viewModel { (screenData: MethodSelectionScreenData) ->
            MethodSelectionViewModel(screenData = screenData)
        }
        factory {
            GenerateTokenWithCardIdUseCase()
        }
        factory {
            CardPaymentScreenStateFactory(stringProvider = get())
        }
        factory<ReviewConfirmRemoteDataSource> {
            ReviewConfirmRemoteDataSourceImpl(service = get())
        }
        viewModel { (checkoutConfiguration: CheckoutConfiguration?) ->
            CardPaymentViewModel(
                checkoutConfiguration = checkoutConfiguration,
                getCardBinUseCase = GetCardBinUseCase(repository = get()),
                initializeCardFormUseCase = get(),
                generateTokenUseCase = GenerateTokenUseCase(),
                processOrderUseCase = ProcessOrderUseCase(repository = get()),
                cardPaymentScreenStateFactory = get(),
            )
        }
        viewModel { (checkoutConfiguration: CheckoutConfiguration?) ->
            PaymentBrickViewModel(
                checkoutConfiguration = checkoutConfiguration,
                fetchInitializationUseCase = get(),
                processOrderUseCase = ProcessOrderUseCase(repository = get()),
                getSecurityCodeScreenUseCase = get(),
                fetchMethodSelectionScreenUseCase = get(),
                cancelledPaymentContextUseCase = get(),
            )
        }
        viewModel { (config: SecurityCodeScreenConfig) ->
            SecurityCodeViewModel(
                config = config,
                generateTokenUseCase = get(),
                cancelledPaymentContextUseCase = get(),
            )
        }
        viewModel { params ->
            val (
                installmentData: MPInstallmentData,
                paymentData: MPPaymentData,
                checkoutType: String,
            ) = params
            InstallmentsViewModel(
                installmentData = installmentData,
                paymentData = paymentData,
                checkoutType = checkoutType,
                orderId = (params.component4() as? String).orEmpty(),
            )
        }
        viewModel { params ->
            val processOrderParams: ProcessOrderParams = params.component1()
            val checkoutConfiguration: CheckoutConfiguration? = params.component2()
            ReviewConfirmViewModel(
                processOrderParams = processOrderParams,
                sellerInfo = checkoutConfiguration.getSellerInfo(),
                fetchReviewConfirmUseCase = FetchReviewConfirmUseCase(
                    repository = ReviewConfirmRepositoryImpl(
                        dataSource = get(),
                    ),
                ),
                processOrderUseCase = ProcessOrderUseCase(repository = get()),
                cancelledPaymentContextUseCase = get(),
            )
        }
    }
