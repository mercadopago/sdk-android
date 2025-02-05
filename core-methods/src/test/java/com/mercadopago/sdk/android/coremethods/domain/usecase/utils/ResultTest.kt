package com.mercadopago.sdk.android.coremethods.domain.usecase.utils

import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import org.junit.Assert.assertEquals
import org.junit.Test

internal class ResultTest {

    @Test
    fun `test Result Success`() {
        val successData = "Success Data"
        val result = Result.Success(successData)

        // Verificando se o resultado é do tipo Success e se o dado está correto
        assertEquals(successData, (result).data)
    }

    @Test
    fun `test Result Error`() {
        val errorData = "Error Data"
        val result = Result.Error(errorData)

        // Verificando se o resultado é do tipo Error e se o erro está correto
        assertEquals(errorData, (result).error)
    }
}
