package com.mercadopago.sdk.android.threeds.di.adapters

import android.content.Context
import com.mercadopago.sdk.android.threeds.data.adapter.ThreeDSWrapperImpl
import com.mercadopago.sdk.android.threeds.domain.adapter.ThreeDSWrapper
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
            modules(provideWrapperModule(mockContext))
        }

        // When
        val adapter: ThreeDSWrapper by inject()

        // Then
        assertNotNull(adapter)
        assertTrue(adapter is ThreeDSWrapperImpl)

        // Clean up
        stopKoin()
    }

    @Test
    fun `when adapters module is loaded multiple times Then should provide same instance`() {
        // Given
        startKoin {
            modules(provideWrapperModule(mockContext))
        }

        // When
        val adapter1: ThreeDSWrapper by inject()
        val adapter2: ThreeDSWrapper by inject()

        // Then
        assertNotNull(adapter1)
        assertNotNull(adapter2)
        // Single scope should provide same instance
        assertTrue(adapter1 === adapter2)

        // Clean up
        stopKoin()
    }

    @Test
    fun `when provideAdaptersModule is called with context Then should return valid module`() {
        // When
        val module = provideWrapperModule(mockContext)

        // Then
        assertNotNull(module)
    }
}
