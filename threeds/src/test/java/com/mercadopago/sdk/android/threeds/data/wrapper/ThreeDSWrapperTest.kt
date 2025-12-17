package com.mercadopago.sdk.android.threeds.data.wrapper

import android.content.Context
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class ThreeDSWrapperTest {
    private lateinit var mockContext: Context
    private lateinit var threeDSWrapper: ThreeDSWrapper

    @Before
    fun setUp() {
        mockContext = mockk(relaxed = true)
        threeDSWrapper = ThreeDSWrapper(mockContext)
    }

    @Test
    fun `ThreeDSWrapper should be created with context`() {
        // Act & Assert
        assertNotNull(threeDSWrapper)
    }

    @Test
    fun `ThreeDSWrapper constructor should accept context parameter`() {
        // Arrange
        val newMockContext = mockk<Context>(relaxed = true)

        // Act
        val newWrapper = ThreeDSWrapper(newMockContext)

        // Assert
        assertNotNull(newWrapper)
    }
}
