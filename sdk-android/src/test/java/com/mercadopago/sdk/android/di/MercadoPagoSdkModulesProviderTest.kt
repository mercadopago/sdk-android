package com.mercadopago.sdk.android.di

import android.app.Application
import android.content.pm.ApplicationInfo
import com.google.gson.Gson
import com.mercadopago.sdk.android.core.di.CoreKoinFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import org.junit.Test
import org.koin.android.ext.koin.androidContext
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
        mockkStatic(ApplicationInfo::class)
        val context = mockk<Application>()
        every {
            context.applicationInfo
        } returns mockk(relaxed = true)
        every {
            context.applicationContext
        } returns context
        every { CoreKoinFactory.createKoinApp(any(), any(), any()) } returns mockk()
        val modulesProvider = MercadoPagoSdkModulesProvider(
            publicKey = "public_key",
            context = context,
        )

        // When
        val module = modulesProvider
            .provideModules()
            .toModule()
        val koin = koinApplication {
            androidContext(context)
            modules(module)
        }

        // Then
        module.verify(extraTypes = listOf(Gson::class))
        koin.checkModules()
    }
}
