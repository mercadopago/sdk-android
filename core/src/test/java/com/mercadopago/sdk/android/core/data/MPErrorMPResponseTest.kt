package com.mercadopago.sdk.android.core.data

import com.mercadopago.sdk.android.core.data.remote.response.MPErrorResponse
import org.junit.Assert.assertEquals
import org.junit.Test

class MPErrorMPResponseTest {
    @Test
    fun `test MPErrorResponse creation`() {
        val code = "404"
        val message = "Not Found"

        val errorResponse = MPErrorResponse(code, message)

        assertEquals(code, errorResponse.code)
        assertEquals(message, errorResponse.message)
    }
}
