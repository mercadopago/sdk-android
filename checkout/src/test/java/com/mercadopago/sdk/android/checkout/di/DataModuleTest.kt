package com.mercadopago.sdk.android.checkout.di

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.res.Configuration
import com.mercadopago.sdk.android.checkout.core.model.CardFormConfiguration
import com.mercadopago.sdk.android.checkout.core.model.CheckoutType
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.data.remote.service.CardFormService
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardDataByBinUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardIssuersUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetInstallmentsUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetPaymentMethodsUseCase
import com.mercadopago.sdk.android.checkout.presentation.usecase.CancelledFormContextUseCase
import com.mercadopago.sdk.android.checkout.presentation.usecase.GenerateTokenUseCase
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.InstallmentsViewModel
import com.mercadopago.sdk.android.coremethods.domain.interactor.CoreMethods
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.test.check.checkModules
import org.koin.test.mock.MockProvider
import org.koin.test.verify.verify
import kotlin.test.Test

internal class DataModuleTest {
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

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun `when provideDataModule is called then bindings should be verified`() {
        val configuration = mockk<Configuration>(relaxed = true)
        every { anyConstructed<Configuration>().setLocale(any()) } returns Unit

        val resources = mockk<android.content.res.Resources>(relaxed = true)
        every { resources.configuration } returns configuration

        val context = mockk<Application>(relaxed = true)
        every { context.applicationInfo } returns mockk(relaxed = true)
        every { context.applicationContext } returns context
        every { context.resources } returns resources
        every { context.createConfigurationContext(any()) } returns context

        val checkoutConfiguration = CheckoutConfiguration(
            checkoutType = CheckoutType.CardForm(CardFormConfiguration()),
            paymentMethods = emptyList(),
        )

        val module = module {
            includes(provideDataModule())
            single { checkoutConfiguration }
            single { mockk<CardFormService>(relaxed = true) }
        }

        val koin = koinApplication {
            androidContext(context)
            modules(module)
        }

        module.verify(
            extraTypes = listOf(
                CheckoutConfiguration::class,
                CheckoutType::class,
                List::class,
                CardFormService::class,
                CoreMethods::class,
                GetCardDataByBinUseCase::class,
                GetPaymentMethodsUseCase::class,
                GetCardIssuersUseCase::class,
                GetInstallmentsUseCase::class,
                GenerateTokenUseCase::class,
                CancelledFormContextUseCase::class,
            ),
        )

        koin.checkModules {
            withInstance<CheckoutConfiguration>(checkoutConfiguration)
        }
    }

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun `when provideInstallmentsModule is called then InstallmentsViewModel should be provided`() {
        val module = module {
            includes(provideInstallmentsModule())
        }

        val koin = koinApplication {
            modules(module)
        }

        module.verify(
            extraTypes = listOf(
                CoreMethods::class,
                InstallmentsViewModel::class,
            ),
        )

        koin.checkModules()
    }
}
