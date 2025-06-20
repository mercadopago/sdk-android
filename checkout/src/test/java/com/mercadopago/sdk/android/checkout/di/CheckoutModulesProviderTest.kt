package com.mercadopago.sdk.android.checkout.di

import android.app.Application
import android.content.pm.ApplicationInfo
import com.mercadopago.sdk.android.core.di.CoreKoinFactory
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
import org.koin.test.verify.verify
import kotlin.test.Test

internal class CheckoutModulesProviderTest {
    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun `when provideModules is called Then modules should be verified`() {
        // Given
        mockkObject(MercadoPagoSDK.Companion)
        mockkStatic(ApplicationInfo::class)
        mockkObject(CoreKoinFactory)
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
        val module = module {
            includes(modulesProvider.provideModules())
            includes(mercadoPagoSdkModulesProvider.provideModules())
        }
        val koin = koinApplication {
            androidContext(context)
            modules(module)
        }

        // Then
        module.verify()
        koin.checkModules()
    }
}
