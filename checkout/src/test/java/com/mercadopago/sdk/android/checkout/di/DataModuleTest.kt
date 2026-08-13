package com.mercadopago.sdk.android.checkout.di

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.res.Configuration
import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPOrder
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.data.remote.request.ReviewConfirmRequest
import com.mercadopago.sdk.android.checkout.data.remote.service.CardFormService
import com.mercadopago.sdk.android.checkout.data.remote.service.OrderService
import com.mercadopago.sdk.android.checkout.data.remote.service.PaymentBrickInitializationService
import com.mercadopago.sdk.android.checkout.data.remote.service.ReviewConfirmService
import com.mercadopago.sdk.android.checkout.domain.model.CardFormInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionScreenData
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.checkout.domain.repository.PaymentBrickInitializationRepository
import com.mercadopago.sdk.android.checkout.domain.usecase.FetchReviewConfirmUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardBinUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.ProcessOrderUseCase
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeScreenConfig
import com.mercadopago.sdk.android.checkout.presentation.usecase.GenerateTokenUseCase
import com.mercadopago.sdk.android.checkout.presentation.validation.SecurityCodeVerifier
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.InstallmentsAnalyticsTracker
import com.mercadopago.sdk.android.checkout.utils.MainDispatcherRule
import com.mercadopago.sdk.android.coremethods.domain.interactor.CoreMethods
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.test.check.checkModules
import org.koin.test.mock.MockProvider
import org.koin.test.verify.verify
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class DataModuleTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        MockProvider.register { mockk(relaxed = true) }
        mockkObject(MercadoPagoSDK.Companion)
        mockkStatic(ApplicationInfo::class)
        mockkConstructor(Configuration::class)
        every { MercadoPagoSDK.getInstance() } returns mockk(relaxed = true)
        every { MercadoPagoSDK.countryCode } returns mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    private fun createMockContext(): Application {
        val configuration = mockk<Configuration>(relaxed = true)
        every { anyConstructed<Configuration>().setLocale(any()) } returns Unit
        val resources = mockk<android.content.res.Resources>(relaxed = true)
        every { resources.configuration } returns configuration
        return mockk<Application>(relaxed = true).also { ctx ->
            every { ctx.applicationInfo } returns mockk(relaxed = true)
            every { ctx.applicationContext } returns ctx
            every { ctx.resources } returns resources
            every { ctx.createConfigurationContext(any()) } returns ctx
        }
    }

    @Suppress("LongMethod")
    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun `when provideDataModule is called then bindings should be verified`() {
        val context = createMockContext()

        val checkoutConfiguration = CheckoutConfiguration(
            checkoutType = MPCheckoutType.CardTransaction(
                MPOrder(
                    orderId = "test",
                    clientToken = "test-token",
                ),
            ),
            paymentMethodConfigs = emptyList(),
        )

        val module = module {
            includes(provideDataModule())
            single { checkoutConfiguration }
            single { mockk<CardFormService>(relaxed = true) }
            single { mockk<OrderService>(relaxed = true) }
            single { mockk<PaymentBrickInitializationService>(relaxed = true) }
            single { mockk<ReviewConfirmService>(relaxed = true) }
        }

        val koin = koinApplication {
            androidContext(context)
            modules(module)
        }

        module.verify(
            extraTypes = listOf(
                CheckoutConfiguration::class,
                MPCheckoutType::class,
                List::class,
                CardFormService::class,
                OrderService::class,
                PaymentBrickInitializationService::class,
                ReviewConfirmService::class,
                PaymentBrickInitializationRepository::class,
                ReviewConfirmRequest::class,
                ProcessOrderParams::class,
                FetchReviewConfirmUseCase::class,
                GetCardBinUseCase::class,
                GenerateTokenUseCase::class,
                CardFormInitializationOutput::class,
                MPInstallmentData::class,
                MPPaymentData::class,
                String::class,
                InstallmentsAnalyticsTracker::class,
                ProcessOrderUseCase::class,
                SecurityCodeScreenConfig::class,
                CoreMethods::class,
                SecurityCodeVerifier::class,
                MethodSelectionScreenData::class,
            ),
        )

        koin.checkModules {
            withInstance<CheckoutConfiguration>(checkoutConfiguration)
            withInstance<CardFormInitializationOutput>(mockk(relaxed = true))
            withInstance<MPInstallmentData>(mockk(relaxed = true))
            withInstance<MPPaymentData>(mockk(relaxed = true))
            withInstance<String>("card_form")
            withInstance<SecurityCodeScreenConfig>(mockk(relaxed = true))
            withInstance<MethodSelectionScreenData>(mockk(relaxed = true))
            withInstance<ReviewConfirmRequest>(mockk(relaxed = true))
            withInstance<ProcessOrderParams>(mockk(relaxed = true))
        }
    }
}
