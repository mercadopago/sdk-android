package com.mercadopago.sdk.android.core.di

import junit.framework.TestCase.assertNotNull
import org.junit.Test

class RetrofitFactoryTest {

    @Test
    fun `test RetrofitServiceFactory creation`() {
        val publicKey = "your_public_key"
        val url = "https://yourapi.com/"

        // Criar uma instância do RetrofitServiceFactory
        val factory = RetrofitServiceFactory(publicKey, url)

        // Verificar se a instância do Retrofit foi criada corretamente
        val someService = factory.createService(SomeService::class.java)

        // Verificamos se o serviço criado não é nulo (verificando a injeção de dependência)
        assertNotNull(someService)
    }

    // Exemplo de interface de serviço que você esperaria criar
    interface SomeService {
        // Defina suas funções de serviço Retrofit aqui
    }
}
