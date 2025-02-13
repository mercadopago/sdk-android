package com.mercadopago.sdk.android.coremethods.di

import com.mercadopago.sdk.android.coremethods.data.datasource.remote.CoreMethodsRemoteDataSource
import com.mercadopago.sdk.android.coremethods.data.remote.service.CoreMethodsService
import com.mercadopago.sdk.android.coremethods.di.datasource.provideDataSourceModule
import com.mercadopago.sdk.android.coremethods.di.repository.provideRepositoryModule
import com.mercadopago.sdk.android.coremethods.di.services.provideNetworkModule
import com.mercadopago.sdk.android.coremethods.di.usecases.provideUseCaseModule
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import com.mercadopago.sdk.android.coremethods.domain.usecase.GenerateCardTokenUseCase
import junit.framework.TestCase.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.java.KoinJavaComponent.inject
import org.koin.test.KoinTest

internal class ProvidersModuleTest : KoinTest {
    @Before
    fun start() {
        // Inicia o Koin com o módulo
        startKoin {
            modules(
                listOf(
                    provideNetworkModule("your_public_key", "https://yourapi.com/"),
                    provideDataSourceModule(),
                    provideRepositoryModule(),
                    provideUseCaseModule(),
                ),
            )
        }
    }

    @After
    fun finish() {
        stopKoin()
    }

    @Test
    fun `when provideUseCaseModule then provides GenerateCardTokenUseCase`() {
        // Tenta injetar o GenerateCardTokenUseCase
        val useCase: GenerateCardTokenUseCase by inject(GenerateCardTokenUseCase::class.java)

        // Verifica se a instância não é nula
        assertNotNull(useCase)
    }

    @Test
    fun `when provideNetworkModule then provides CoreMethodsService`() {
        // Tenta injetar o GenerateCardTokenUseCase
        val network: CoreMethodsService by inject(CoreMethodsService::class.java)

        // Verifica se a instância não é nula
        assertNotNull(network)
    }

    @Test
    fun `when provideRepositoryModule then provides CoreMethodsRepository`() {
        // Tenta injetar o GenerateCardTokenUseCase
        val repository: CoreMethodsRepository by inject(CoreMethodsRepository::class.java)

        // Verifica se a instância não é nula
        assertNotNull(repository)
    }

    @Test
    fun `when provideDataSourceModule then provides CoreMethodsRemoteDataSource`() {
        // Tenta injetar o GenerateCardTokenUseCase
        val datasource: CoreMethodsRemoteDataSource by inject(CoreMethodsRemoteDataSource::class.java)

        // Verifica se a instância não é nula
        assertNotNull(datasource)
    }
}
