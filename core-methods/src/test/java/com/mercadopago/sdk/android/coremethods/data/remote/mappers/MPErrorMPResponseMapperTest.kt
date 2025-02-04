package com.mercadopago.sdk.android.coremethods.data.remote.mappers

import com.mercadopago.sdk.android.core.data.remote.response.MPErrorResponse
import org.junit.Assert.assertEquals
import kotlin.test.Test

internal class MPErrorMPResponseMapperTest {

    @Test
    fun `test toResultError conversion`() {
        val mpErrorResponse = MPErrorResponse(code = "404", message = "Not Found")

        val resultError = mpErrorResponse.toResultError()

        assertEquals("404", resultError.code)
        assertEquals("Not Found", resultError.message)
    }
}
