package com.mercadopago.sdk.android.core.di

import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.koin.core.Koin
import org.koin.core.module.Module
import org.koin.dsl.module

class CoreKoinFactoryTest {

    private class TestModuleProvider(override val koinApp: Koin) : CoreKoinModuleProvider {
        override fun provideModules(): List<Module> {
            return listOf(module { /* Definitions for testing */ })
        }
    }

    @Test
    fun `test CoreKoinFactory createKoinApp`() {
        val provider = TestModuleProvider()
        val koin: Koin = CoreKoinFactory.createKoinApp(provider)
        assertNotNull(koin)
    }
}
