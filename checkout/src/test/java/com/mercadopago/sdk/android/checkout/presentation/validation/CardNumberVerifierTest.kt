package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberState
import com.mercadopago.sdk.android.checkout.presentation.state.ValidationState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class CardNumberVerifierTest {
    private val verifier = CardNumberVerifier()

    @Test
    fun `when length is 0 then returns required field error`() {
        val state = CardNumberState(
            length = 0,
            maxLength = 16,
            validation = ValidationState(errorEmpty = "required"),
        )

        val result = verifier.verify(state)

        assertEquals("required", result)
    }

    @Test
    fun `when length greater than 0 and less than maxLength then returns incomplete error`() {
        val state = CardNumberState(
            length = 8,
            maxLength = 16,
            validation = ValidationState(errorIncomplete = "incomplete"),
        )

        val result = verifier.verify(state)

        assertEquals("incomplete", result)
    }

    @Test
    fun `when length equals maxLength then returns empty string`() {
        val result = verifier.verify(CardNumberState(length = 16, maxLength = 16))

        assertTrue(result.isEmpty())
    }
}
