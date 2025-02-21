package com.mercadopago.sdk.android.core.di

import android.content.Context
import android.util.Log
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.koin.core.Koin
import org.koin.core.module.Module
import org.koin.dsl.module

class CoreKoinFactoryTest {

    private class TestModuleProvider : CoreKoinModuleProvider {
        override val koinApp: Koin = CoreKoinFactory.createKoinApp(
            provider = this,
            context = mockk<Context>(relaxed = true),
        )

        override fun provideModules(): List<Module> {
            return listOf(module { /* Definitions for testing */ })
        }
    }

    @Test
    fun `test CoreKoinFactory createKoinApp`() {
        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        val provider = TestModuleProvider()

        assertNotNull(provider.koinApp)
    }
}
