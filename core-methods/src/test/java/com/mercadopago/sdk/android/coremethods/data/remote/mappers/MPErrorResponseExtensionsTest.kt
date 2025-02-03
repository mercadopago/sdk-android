package com.mercadopago.sdk.android.coremethods.data.remote.mappers

import com.mercadopago.sdk.android.core.data.remote.response.MPErrorResponse
import org.junit.Assert.assertEquals
import kotlin.test.Test

internal class MPErrorResponseExtensionsTest {

    @Test
    fun `test toResultError conversion`() {
        // Cria uma instância de MPErrorResponse
        val mpErrorResponse = MPErrorResponse(code = "404", message = "Not Found")

        // Chama a função de extensão
        val resultError = mpErrorResponse.toResultError()

        // Verifica se os valores estão corretos
        assertEquals("404", resultError.code)
        assertEquals("Not Found", resultError.message)
    }
}
