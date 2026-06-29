package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.sdk.android.checkout.presentation.state.ExpirationDateState
import com.mercadopago.sdk.android.checkout.presentation.state.ValidationState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class ExpirationDateVerifierTest {
    private val verifier = ExpirationDateVerifier()

    private val defaultValidation = ValidationState(
        errorEmpty = "required",
        errorIncomplete = "incomplete",
        errorInvalid = "invalid date",
    )

    @Test
    fun `when length is 0 then returns required field error`() {
        val state = ExpirationDateState(length = 0, validation = defaultValidation)
        val result = verifier.verify(state)

        assertEquals("required", result)
    }

    @Test
    fun `when length greater than 0 and not filled then returns incomplete error`() {
        val state = ExpirationDateState(length = 2, filled = false, validation = defaultValidation)
        val result = verifier.verify(state)

        assertEquals("incomplete", result)
    }

    @Test
    fun `when filled and isValid is false then returns invalid date error`() {
        val state = ExpirationDateState(length = 4, filled = true, isValid = false, validation = defaultValidation)
        val result = verifier.verify(state)

        assertEquals("invalid date", result)
    }

    @Test
    fun `when filled and isValid is true then returns empty string`() {
        val state = ExpirationDateState(length = 4, filled = true, isValid = true, validation = defaultValidation)
        val result = verifier.verify(state)

        assertTrue(result.isEmpty())
    }
}
