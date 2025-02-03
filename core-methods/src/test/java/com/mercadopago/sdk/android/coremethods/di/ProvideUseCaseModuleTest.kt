package com.mercadopago.sdk.android.coremethods.di

import com.mercadopago.sdk.android.coremethods.di.usecases.provideUseCaseModule
import com.mercadopago.sdk.android.coremethods.domain.usecase.GenerateCardTokenUseCase
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.java.KoinJavaComponent.inject

class ProvideUseCaseModuleTest : KoinTest {

    @Test
    fun `test provideUseCaseModule provides GenerateCardTokenUseCase`() {
        // Inicia o Koin com o módulo
        startKoin {
            modules(provideUseCaseModule())
        }

        // Tenta injetar o GenerateCardTokenUseCase
        val useCase: GenerateCardTokenUseCase by inject()

        // Verifica se a instância não é nula
        assertNotNull(useCase)

        // Encerra o Koin após o teste
        stopKoin()
    }
}
