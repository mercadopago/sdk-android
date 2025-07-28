package com.mercadopago.sdk.android.threeds.domain.exceptions

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MPThreeDSExceptionsTest {

    @Test
    fun `when MPThreeDSAlreadyInitializedException is created Then message should be correct`() {
        // When
        val exception = MPThreeDSAlreadyInitializedException()

        // Then
        assertTrue(exception is RuntimeException)
        assertEquals(
            "MPThreeDS is already initialized. Call clearInstance() first if you need to reinitialize.",
            exception.message
        )
    }

    @Test
    fun `when MPThreeDSNotInitializedException is created Then message should be correct`() {
        // When
        val exception = MPThreeDSNotInitializedException()

        // Then
        assertTrue(exception is RuntimeException)
        assertEquals(
            "MPThreeDS is not initialized. Call MPThreeDS.initialize(context) first.",
            exception.message
        )
    }
}
