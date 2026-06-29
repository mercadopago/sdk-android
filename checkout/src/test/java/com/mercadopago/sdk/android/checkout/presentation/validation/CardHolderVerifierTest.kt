package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.sdk.android.checkout.presentation.state.CardHolderState
import com.mercadopago.sdk.android.checkout.presentation.state.ValidationState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class CardHolderVerifierTest {
    private val verifier = CardHolderVerifier()

    @Test
    fun `when value is empty then returns required field error`() {
        val state = CardHolderState(value = "", validation = ValidationState(errorEmpty = "required"))

        val result = verifier.verify(state)

        assertEquals("required", result)
    }

    @Test
    fun `when value length is less than min characters then returns incomplete error`() {
        val state = CardHolderState(value = "AB", validation = ValidationState(errorIncomplete = "incomplete"))

        val result = verifier.verify(state)

        assertEquals("incomplete", result)
    }

    @Test
    fun `when value contains special characters then returns format error`() {
        val state = CardHolderState(value = "AB@", validation = ValidationState(errorInvalid = "invalid format"))

        val result = verifier.verify(state)

        assertEquals("invalid format", result)
    }

    @Test
    fun `when value is valid then returns empty string`() {
        val result = verifier.verify(CardHolderState(value = "John Doe"))

        assertTrue(result.isEmpty())
    }
}
