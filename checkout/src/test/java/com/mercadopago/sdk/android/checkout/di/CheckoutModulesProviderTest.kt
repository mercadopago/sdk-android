package com.mercadopago.sdk.android.checkout.di

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.res.Configuration
import com.google.gson.Gson
import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPOrder
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferences
import com.mercadopago.sdk.android.checkout.domain.model.CardFormInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardBinUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.ProcessOrderUseCase
import com.mercadopago.sdk.android.checkout.presentation.factory.CardPaymentScreenStateFactory
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeScreenConfig
import com.mercadopago.sdk.android.checkout.presentation.usecase.CancelledFormContextUseCase
import com.mercadopago.sdk.android.checkout.presentation.usecase.GenerateTokenUseCase
import com.mercadopago.sdk.android.checkout.presentation.validation.SecurityCodeVerifier
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardPaymentViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.InstallmentsAnalyticsTracker
import com.mercadopago.sdk.android.checkout.utils.MainDispatcherRule
import com.mercadopago.sdk.android.core.di.CoreKoinFactory
import com.mercadopago.sdk.android.coremethods.domain.interactor.CoreMethods
import com.mercadopago.sdk.android.di.MercadoPagoSdkModulesProvider
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
internal class CheckoutModulesProviderTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        MockProvider.register { mockk(relaxed = true) }
        mockkObject(MercadoPagoSDK.Companion)
        mockkStatic(ApplicationInfo::class)
        mockkObject(CoreKoinFactory)
        mockkObject(MPCheckoutType::class)
        mockkObject(CardPaymentScreenStateFactory::class)
        mockkConstructor(Configuration::class)
    }

    private fun createMockContext(): Application {
        val configuration = mockk<Configuration>(relaxed = true)
        every { configuration.setLocale(any()) } returns Unit
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

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun `when provideModules is called Then modules should be verified`() {
        val context = createMockContext()
        every { MercadoPagoSDK.getInstance() } returns mockk<MercadoPagoSDK>(relaxed = true)
        every { CoreKoinFactory.createKoinApp(any(), any(), any()) } returns mockk()
        val modulesProvider = CheckoutModulesProvider(context = context)
        val mercadoPagoSdkModulesProvider = MercadoPagoSdkModulesProvider(
            publicKey = "public_key",
            context = context,
        )

        val checkoutConfiguration = CheckoutConfiguration(
            checkoutType = MPCheckoutType.CardTransaction(
                MPOrder(
                    orderId = "",
                    clientToken = "test-token",
                ),
            ),
            paymentMethodConfigs = emptyList(),
        )
        val module = module {
            includes(modulesProvider.provideModules())
            includes(mercadoPagoSdkModulesProvider.provideModules())
            single { checkoutConfiguration }
        }
        val koin = koinApplication {
            androidContext(context)
            modules(module)
        }

        module.verify(
            extraTypes = listOf(
                CoreMethods::class,
                MPCheckoutType::class,
                List::class,
                CheckoutConfiguration::class,
                CheckoutThemePreferences::class,
                CardPaymentViewModel::class,
                GetCardBinUseCase::class,
                GenerateTokenUseCase::class,
                ProcessOrderUseCase::class,
                CancelledFormContextUseCase::class,
                Gson::class,
                CardFormInitializationOutput::class,
                MPInstallmentData::class,
                MPPaymentData::class,
                String::class,
                InstallmentsAnalyticsTracker::class,
                SecurityCodeScreenConfig::class,
                SecurityCodeVerifier::class,
            ),
        )
        koin.checkModules {
            withInstance<CheckoutConfiguration>(checkoutConfiguration)
            withInstance<CardFormInitializationOutput>(mockk(relaxed = true))
            withInstance<MPInstallmentData>(mockk(relaxed = true))
            withInstance<MPPaymentData>(mockk(relaxed = true))
            withInstance<String>("card_form")
            withInstance<SecurityCodeScreenConfig>(mockk(relaxed = true))
        }
    }
}
