package com.mercadopago.sdk.android.coremethods.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

internal class MPErrorTest {
    @Test
    fun `test ResultError creation with default values`() {
        val result = MPError.Request(code = "100", message = "error")

        assertEquals("error", result.message)
        assertEquals("100", result.code)
    }

    @Test
    fun `test ResultError creation with specified values`() {
        val customMessage = "An error occurred"
        val customCode = "404"

        val result = MPError.Request(message = customMessage, code = customCode)

        assertEquals(customMessage, result.message)
        assertEquals(customCode, result.code)
    }

    @Test
    fun `test ResultError equality`() {
        val message = "An error occurred"
        val code = "400"

        val result1 = MPError.Request(message = message, code = code)
        val result2 = MPError.Request(message = message, code = code)

        assertEquals(result1, result2)
    }

    @Test
    fun `Validation should hold the correct message`() {
        val expectedMessage = "This field is required"
        val validationError = MPError.Validation(message = expectedMessage)

        val actualMessage = validationError.message

        assertEquals(expectedMessage, actualMessage)
    }

    @Test
    fun `Validation should be equal to another instance with the same message`() {
        val message = "This field is required"
        val validationError1 = MPError.Validation(message = message)
        val validationError2 = MPError.Validation(message = message)

        assertEquals(validationError1, validationError2)
    }

    @Test
    fun `Validation should not be equal to another result class`() {
        val validationError = MPError.Validation(message = "Error")
        val differentError = MPError.Validation(message = "Different Error")

        assert(validationError != differentError)
    }
}
