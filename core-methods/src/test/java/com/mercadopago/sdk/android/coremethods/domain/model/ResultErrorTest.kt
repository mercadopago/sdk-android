package com.mercadopago.sdk.android.coremethods.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

internal class ResultErrorTest {
    @Test
    fun `test ResultError creation with default values`() {
        val resultError = ResultError.Request(code = "100", message = "error")

        assertEquals("error", resultError.message)
        assertEquals("100", resultError.code)
    }

    @Test
    fun `test ResultError creation with specified values`() {
        val customMessage = "An error occurred"
        val customCode = "404"

        val resultError = ResultError.Request(message = customMessage, code = customCode)

        assertEquals(customMessage, resultError.message)
        assertEquals(customCode, resultError.code)
    }

    @Test
    fun `test ResultError equality`() {
        val message = "An error occurred"
        val code = "400"

        val resultError1 = ResultError.Request(message = message, code = code)
        val resultError2 = ResultError.Request(message = message, code = code)

        assertEquals(resultError1, resultError2)
    }

    @Test
    fun `Validation should hold the correct message`() {
        val expectedMessage = "This field is required"
        val validationError = ResultError.Validation(message = expectedMessage)

        val actualMessage = validationError.message

        assertEquals(expectedMessage, actualMessage)
    }

    @Test
    fun `Validation should be equal to another instance with the same message`() {
        val message = "This field is required"
        val validationError1 = ResultError.Validation(message = message)
        val validationError2 = ResultError.Validation(message = message)

        assertEquals(validationError1, validationError2)
    }

    @Test
    fun `Validation should not be equal to another result class`() {
        val validationError = ResultError.Validation(message = "Error")
        val differentError = ResultError.Validation(message = "Different Error")

        assert(validationError != differentError)
    }
}
