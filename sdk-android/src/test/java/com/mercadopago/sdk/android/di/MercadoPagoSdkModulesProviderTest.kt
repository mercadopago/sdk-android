package com.mercadopago.sdk.android.di

import android.content.Context
import com.mercadopago.sdk.android.core.di.CoreKoinFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinApplication
import org.koin.test.KoinTest
import org.koin.test.check.checkModules
import org.koin.test.verify.verify

internal class MercadoPagoSdkModulesProviderTest : KoinTest {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun `when provideModules is called Then modules should be verified`() {
        // Given
        mockkObject(CoreKoinFactory)
        every { CoreKoinFactory.createKoinApp(any(), any(), any()) } returns mockk()
        val modulesProvider = MercadoPagoSdkModulesProvider(
            publicKey = "public_key",
            context = mockk<Context>(),
        )

        // When
        val module = modulesProvider
            .provideModules()
            .toModule()
        val koin = koinApplication {
            modules(module)
        }

        // Then
        module.verify()
        koin.checkModules()
    }
}
