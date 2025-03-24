package com.mercadopago.sdk.android.coremethods.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

internal class ResultErrorTest {
    @Test
    fun `test ResultError creation with default values`() {
        val resultError = ResultError()

        assertEquals("", resultError.message)
        assertEquals("", resultError.code)
    }

    @Test
    fun `test ResultError creation with specified values`() {
        val customMessage = "An error occurred"
        val customCode = "404"

        val resultError = ResultError(message = customMessage, code = customCode)

        assertEquals(customMessage, resultError.message)
        assertEquals(customCode, resultError.code)
    }

    @Test
    fun `test ResultError equality`() {
        val message = "An error occurred"
        val code = "400"

        val resultError1 = ResultError(message = message, code = code)
        val resultError2 = ResultError(message = message, code = code)

        // Verifica se as duas instâncias são consideradas iguais
        assertEquals(resultError1, resultError2)
    }
}
