package com.mercadopago.sdk.android.coremethods.di

import junit.framework.TestCase.assertNotNull
import org.koin.core.context.GlobalContext.stopKoin
import kotlin.test.Test

internal class CoreMethodsModulesProviderTest {

    @Test
    fun `test CoreMethodsModulesProvider provides GenerateCardTokenUseCase`() {
        // Define a chave pública de teste
        val publicKey = "test_public_key"

        // Cria uma instância do CoreMethodsModulesProvider
        val provider = CoreMethodsModulesProvider(publicKey)

        // Tenta obter a instância de GenerateCardTokenUseCase
        val useCase = provider.provideGenerateCardTokenUseCase()

        // Verifica se a instância não é nula
        assertNotNull(useCase)

        // Encerra o Koin para liberar recursos após o teste
        stopKoin()
    }
}
