package com.mercadopago.sdk.android.checkout.statusscreen.domain.model

import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class StatusScreenContractViolationTest {
    @Test
    fun `given the invalid response error then it is a contract violation`() {
        // Given
        val error = invalidStatusScreenResponseError()

        // When
        val isContractViolation = error.isStatusScreenContractViolation()

        // Then
        assertTrue(isContractViolation)
    }

    @Test
    fun `given an HTTP 422 then it is a contract violation`() {
        // Given
        val error = ResponseError(code = "unprocessable", message = "Unsupported status detail", httpStatus = 422)

        // When
        val isContractViolation = error.isStatusScreenContractViolation()

        // Then
        assertTrue(isContractViolation)
    }

    @Test
    fun `given a server error then it is not a contract violation`() {
        // Given
        val error = ResponseError(code = "internal_error", message = "Internal error", httpStatus = 500)

        // When
        val isContractViolation = error.isStatusScreenContractViolation()

        // Then
        assertFalse(isContractViolation)
    }

    @Test
    fun `given a network error then it is not a contract violation`() {
        // Given
        val error = ResponseError(code = "NO_INTERNET", message = "No internet connection")

        // When
        val isContractViolation = error.isStatusScreenContractViolation()

        // Then
        assertFalse(isContractViolation)
    }
}
