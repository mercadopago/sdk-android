package com.mercadopago.sdk.android.checkout.di

import android.app.Application
import android.content.pm.ApplicationInfo
import com.mercadopago.sdk.android.checkout.core.model.CheckoutType
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferences
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardDataByBinUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardIssuersUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetInstallmentsUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetPaymentMethodsUseCase
import com.mercadopago.sdk.android.checkout.presentation.usecase.GetIdentificationTypesUseCase
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardPaymentViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.InstallmentsViewModel
import com.mercadopago.sdk.android.core.di.CoreKoinFactory
import com.mercadopago.sdk.android.coremethods.domain.interactor.CoreMethods
import com.mercadopago.sdk.android.di.MercadoPagoSdkModulesProvider
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.test.check.checkModules
import org.koin.test.mock.MockProvider
import org.koin.test.verify.verify
import kotlin.test.Test

internal class CheckoutModulesProviderTest {
    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun `when provideModules is called Then modules should be verified`() {
        // Given
        MockProvider.register { mockk(relaxed = true) }
        mockkObject(MercadoPagoSDK.Companion)
        mockkStatic(ApplicationInfo::class)
        mockkObject(CoreKoinFactory)
        mockkObject(CheckoutType::class)
        val context = mockk<Application>()
        every {
            context.applicationInfo
        } returns mockk(relaxed = true)
        every {
            context.applicationContext
        } returns context
        every { MercadoPagoSDK.getInstance() } returns mockk<MercadoPagoSDK>(relaxed = true)
        every { CoreKoinFactory.setKoinModules(any(), any()) } returns mockk()
        every { CoreKoinFactory.createKoinApp(any(), any(), any()) } returns mockk()
        val modulesProvider = CheckoutModulesProvider()
        val mercadoPagoSdkModulesProvider = MercadoPagoSdkModulesProvider(
            publicKey = "public_key",
            context = context,
        )

        // When
        val checkoutConfiguration = CheckoutConfiguration(
            checkoutType = CheckoutType.CardForm(),
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
                GetCardDataByBinUseCase::class,
                GetPaymentMethodsUseCase::class,
                GetCardIssuersUseCase::class,
                GetInstallmentsUseCase::class,
                GetIdentificationTypesUseCase::class,
            ),
        )
        koin.checkModules {
            withInstance<CheckoutConfiguration>(checkoutConfiguration)
        }
    }
}
