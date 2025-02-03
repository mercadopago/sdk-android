package com.mercadopago.sdk.android.coremethods.exceptions

import org.junit.Assert.assertEquals
import org.junit.Test

internal class InitializationExceptionTest {

    @Test
    fun `test default InitializationException message`() {
        // Cria uma instância da exceção
        val exception = InitializationException()

        // Verifica se a mensagem padrão é a esperada
        val expectedMessage = "SDK is not initialized. " +
            "Please start the SDK inside your Application class calling CoreMethods.initialize()"
        assertEquals(expectedMessage, exception.message)
    }

    @Test
    fun `test custom InitializationException message`() {
        // Mensagem personalizada
        val customMessage = "Custom initialization error message"

        // Cria uma instância da exceção com a mensagem personalizada
        val exception = InitializationException(customMessage)

        // Verifica se a mensagem é a personalizada que foi passada
        assertEquals(customMessage, exception.message)
    }
}
