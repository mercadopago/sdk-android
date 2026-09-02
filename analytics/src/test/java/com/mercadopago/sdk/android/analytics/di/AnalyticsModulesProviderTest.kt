package com.mercadopago.sdk.android.analytics.di

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import com.mercadopago.sdk.android.core.di.CoreKoinFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinApplication
import org.koin.test.check.checkModules
import org.koin.test.verify.verify

internal class AnalyticsModulesProviderTest {

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
        } returns mockk(relaxed = true)
        every { CoreKoinFactory.createKoinApp(any(), any(), any()) } returns mockk()
        val modulesProvider = AnalyticsModulesProvider(
            context = context,
            getSiteIdFlow = mockk<Flow<String>>(),
            nativeSiteId = "MLA",
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
        module.verify(
            extraTypes = listOf(
                CoroutineDispatcher::class,
                Context::class,
                String::class,
                Function0::class,
                Function1::class,
            )
        )
        koin.checkModules()
    }
}
