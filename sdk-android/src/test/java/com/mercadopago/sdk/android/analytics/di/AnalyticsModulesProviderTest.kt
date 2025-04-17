package com.mercadopago.sdk.android.analytics.di

import android.content.Context
import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.core.di.CoreKoinFactory
import com.mercadopago.sdk.android.di.MercadoPagoSdkModulesProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.test.check.checkModules
import org.koin.test.verify.verify

internal class AnalyticsModulesProviderTest {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun `when provideModules is called Then modules should be verified`() {
        // Given
        mockkObject(MPAnalytics.Companion)
        every { MPAnalytics.getInstance() } returns mockk<MPAnalytics>(relaxed = true)
        mockkObject(CoreKoinFactory)
        every { CoreKoinFactory.setKoinModules(any(), any()) } returns mockk()
        every { CoreKoinFactory.createKoinApp(any(), any(), any()) } returns mockk()
        val modulesProvider = AnalyticsModulesProvider()
        val mercadoPagoSdkModulesProvider = MercadoPagoSdkModulesProvider(
            publicKey = "public_key",
            context = mockk<Context>(),
        )

        // When
        val module = module {
            includes(modulesProvider.provideModules())
            includes(mercadoPagoSdkModulesProvider.provideModules())
        }
        val koin = koinApplication {
            modules(module)
        }

        // Then
        module.verify()
        koin.checkModules()
    }
}
