package com.mercadopago.sdk.android.coremethods.di

import com.mercadopago.sdk.android.coremethods.di.datasource.provideDataSourceModule
import com.mercadopago.sdk.android.coremethods.di.repository.provideRepositoryModule
import com.mercadopago.sdk.android.coremethods.di.services.provideNetworkModule
import com.mercadopago.sdk.android.coremethods.di.usecases.provideUseCaseModule
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.check.checkModules
import org.koin.test.verify.verify

internal class ProvidersModuleTest : KoinTest {

    val modules = module {
        includes(
            provideDataSourceModule(),
            provideRepositoryModule(),
            provideRepositoryModule(),
            provideNetworkModule(
                "your_public_key",
                "https://yourapi.com/"
            ),
            provideUseCaseModule()
        )
    }

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun `when provideModules then provides GenerateCardTokenUseCase`() {
        val koin = koinApplication {
            modules(modules)
        }
        modules.verify()
        koin.checkModules()
    }
}
