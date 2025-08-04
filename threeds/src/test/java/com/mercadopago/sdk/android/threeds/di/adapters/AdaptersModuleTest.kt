package com.mercadopago.sdk.android.threeds.di.adapters

import android.content.Context
import com.mercadopago.sdk.android.threeds.data.adapter.USDKThreeDSAdapter
import com.mercadopago.sdk.android.threeds.domain.adapter.ThreeDSSDKAdapter
import io.mockk.mockk
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class AdaptersModuleTest : KoinTest {

    private val mockContext = mockk<Context>(relaxed = true)

    @Test
    fun `when adapters module is loaded Then ThreeDSSDKAdapter should be provided`() {
        // Given
        startKoin {
            modules(provideAdaptersModule(mockContext))
        }

        // When
        val adapter: ThreeDSSDKAdapter by inject()

        // Then
        assertNotNull(adapter)
        assertTrue(adapter is USDKThreeDSAdapter)

        // Clean up
        stopKoin()
    }

    @Test
    fun `when adapters module is loaded multiple times Then should provide different instances`() {
        // Given
        startKoin {
            modules(provideAdaptersModule(mockContext))
        }

        // When
        val adapter1: ThreeDSSDKAdapter by inject()
        val adapter2: ThreeDSSDKAdapter by inject()

        // Then
        assertNotNull(adapter1)
        assertNotNull(adapter2)
        // Factory scope should provide different instances
        assertTrue(adapter1 !== adapter2)

        // Clean up
        stopKoin()
    }

    @Test
    fun `when provideAdaptersModule is called with context Then should return valid module`() {
        // When
        val module = provideAdaptersModule(mockContext)

        // Then
        assertNotNull(module)
    }
}
