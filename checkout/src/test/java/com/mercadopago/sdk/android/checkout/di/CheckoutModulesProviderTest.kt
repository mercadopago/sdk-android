package com.mercadopago.sdk.android.checkout.di

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.res.Configuration
import com.google.gson.Gson
import com.mercadopago.sdk.android.checkout.core.model.CardFormConfiguration
import com.mercadopago.sdk.android.checkout.core.model.CheckoutType
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferences
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardBinUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardDataByBinUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardIssuersUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetInstallmentsUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetPaymentMethodsUseCase
import com.mercadopago.sdk.android.checkout.presentation.factory.CardPaymentScreenStateFactory
import com.mercadopago.sdk.android.checkout.presentation.usecase.CancelledFormContextUseCase
import com.mercadopago.sdk.android.checkout.presentation.usecase.GenerateTokenUseCase
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardPaymentViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.InstallmentsViewModel
import com.mercadopago.sdk.android.core.di.CoreKoinFactory
import com.mercadopago.sdk.android.coremethods.domain.interactor.CoreMethods
import com.mercadopago.sdk.android.di.MercadoPagoSdkModulesProvider
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.mockkStatic
import org.junit.Before
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.test.check.checkModules
import org.koin.test.mock.MockProvider
import org.koin.test.verify.verify
import kotlin.test.Test

internal class CheckoutModulesProviderTest {
    @Before
    fun setUp() {
        MockProvider.register { mockk(relaxed = true) }
        mockkObject(MercadoPagoSDK.Companion)
        mockkStatic(ApplicationInfo::class)
        mockkObject(CoreKoinFactory)
        mockkObject(CheckoutType::class)
        mockkObject(CardPaymentScreenStateFactory::class)
        mockkConstructor(Configuration::class)
    }

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun `when provideModules is called Then modules should be verified`() {
        // Given
        val configuration = mockk<Configuration>(relaxed = true)
        every { configuration.setLocale(any()) } returns Unit
        every { anyConstructed<Configuration>().setLocale(any()) } returns Unit

        val resources = mockk<android.content.res.Resources>(relaxed = true)
        every { resources.configuration } returns configuration

        val context = mockk<Application>(relaxed = true)
        every { context.applicationInfo } returns mockk(relaxed = true)
        every { context.applicationContext } returns context
        every { context.resources } returns resources
        every { context.createConfigurationContext(any()) } returns context
        every { MercadoPagoSDK.getInstance() } returns mockk<MercadoPagoSDK>(relaxed = true)
        every { CoreKoinFactory.createKoinApp(any(), any(), any()) } returns mockk()
        val modulesProvider = CheckoutModulesProvider(context = context)
        val mercadoPagoSdkModulesProvider = MercadoPagoSdkModulesProvider(
            publicKey = "public_key",
            context = context,
        )

        // When
        val checkoutConfiguration = CheckoutConfiguration(
            checkoutType = CheckoutType.CardForm(CardFormConfiguration()),
            paymentMethods = emptyList(),
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

        // Then
        module.verify(
            extraTypes = listOf(
                CoreMethods::class,
                CheckoutType::class,
                List::class,
                CheckoutConfiguration::class,
                CheckoutThemePreferences::class,
                CardPaymentViewModel::class,
                InstallmentsViewModel::class,
                GetCardBinUseCase::class,
                GetCardDataByBinUseCase::class,
                GetPaymentMethodsUseCase::class,
                GetCardIssuersUseCase::class,
                GetInstallmentsUseCase::class,
                GenerateTokenUseCase::class,
                CardPaymentScreenStateFactory::class,
                CancelledFormContextUseCase::class,
                Gson::class,
            ),
        )
        koin.checkModules {
            withInstance<CheckoutConfiguration>(checkoutConfiguration)
        }
    }
}
