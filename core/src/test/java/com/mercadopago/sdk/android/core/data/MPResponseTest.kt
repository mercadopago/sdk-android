package com.mercadopago.sdk.android.core.data

import com.mercadopago.sdk.android.core.data.remote.response.MPErrorResponse
import com.mercadopago.sdk.android.core.data.remote.utils.MPResponse
import org.junit.Assert.assertEquals
import org.junit.Test

class MPResponseTest {

    @Test
    fun `test MPResponse Success`() {
        val successResponse = "This is a successful response"
        val response = MPResponse.Success(successResponse)

        assertEquals(successResponse, response.response)
    }

    @Test
    fun `test MPResponse Error`() {
        val errorCode = "404"
        val errorMessage = "Not Found"
        val errorResponse = MPErrorResponse(errorCode, errorMessage)

        val response = MPResponse.Error(errorResponse)

        assertEquals(errorResponse, response.errorResponse)
        assertEquals(errorCode, response.errorResponse.code)
        assertEquals(errorMessage, response.errorResponse.message)
    }
}
