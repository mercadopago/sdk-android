package com.mercadopago.sdk.android.core.di

import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.koin.core.Koin
import org.koin.core.module.Module
import org.koin.dsl.module

class CoreKoinFactoryTest {

    // Implementação de CoreKoinModuleProvider para os testes
    private class TestModuleProvider : CoreKoinModuleProvider {
        override fun provideModules(): List<Module> {
            // Definindo um módulo de teste simples
            return listOf(module { /* Definitions for testing */ })
        }
    }

    @Test
    fun `test CoreKoinFactory createKoinApp`() {
        // Criar um provedor de módulos
        val provider = TestModuleProvider()

        // Criar uma instância do Koin usando o CoreKoinFactory
        val koin: Koin = CoreKoinFactory.createKoinApp(provider)

        // Verificamos se o Koin foi criado corretamente
        assertNotNull(koin)
    }
}
