package com.mercadopago.sdk.android.checkout.di

import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.test.check.checkModules
import org.koin.test.mock.MockProvider
import org.koin.test.verify.verify
import kotlin.test.Test

internal class NetworkModuleTest {
    @Before
    fun setUp() {
        MockProvider.register { mockk(relaxed = true) }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun `when provideNetworkModule is called then bindings should be verified`() {
        val module = module {
            includes(provideNetworkModule("https://api.example.com/"))
        }

        val koin = koinApplication {
            modules(module)
        }

        module.verify(
            extraTypes = listOf(RetrofitFactory::class),
        )

        koin.checkModules()
    }

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun `given different baseUrl then module is still valid`() {
        val module = module {
            includes(provideNetworkModule("https://other-api.mercadopago.com/"))
        }

        koinApplication {
            modules(module)
        }.checkModules()
    }
}
