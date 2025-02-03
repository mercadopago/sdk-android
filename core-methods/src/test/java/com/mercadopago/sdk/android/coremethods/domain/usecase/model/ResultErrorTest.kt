package com.mercadopago.sdk.android.coremethods.domain.usecase.model

import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import org.junit.Assert.assertEquals
import org.junit.Test

class ResultErrorTest {

    @Test
    fun `test ResultError creation with default values`() {
        // Cria uma instância de ResultError com valores padrão
        val resultError = ResultError()

        // Verifica se os valores padrão são os esperados
        assertEquals("", resultError.message)
        assertEquals("", resultError.code)
    }

    @Test
    fun `test ResultError creation with specified values`() {
        // Mensagens de erro personalizadas
        val customMessage = "An error occurred"
        val customCode = "404"

        // Cria uma instância de ResultError com valores especificados
        val resultError = ResultError(message = customMessage, code = customCode)

        // Verifique se os valores estão corretos
        assertEquals(customMessage, resultError.message)
        assertEquals(customCode, resultError.code)
    }

    @Test
    fun `test ResultError equality`() {
        // Cria duas instâncias idênticas
        val message = "An error occurred"
        val code = "400"

        val resultError1 = ResultError(message = message, code = code)
        val resultError2 = ResultError(message = message, code = code)

        // Verifica se as duas instâncias são consideradas iguais
        assertEquals(resultError1, resultError2)
    }
}
