package com.mercadopago.sdk.android.coremethods.exceptions

import org.junit.Assert.assertEquals
import org.junit.Test

internal class InitializationExceptionTest {
    @Test
    fun `test default InitializationException message`() {
        val exception = InitializationException()

        val expectedMessage = "SDK is not initialized. " +
            "Please start the SDK inside your Application class calling CoreMethods.initialize()"
        assertEquals(expectedMessage, exception.message)
    }

    @Test
    fun `test custom InitializationException message`() {
        val customMessage = "Custom initialization error message"

        val exception = InitializationException(customMessage)

        assertEquals(customMessage, exception.message)
    }
}
