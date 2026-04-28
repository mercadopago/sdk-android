package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberState
import com.mercadopago.sdk.android.checkout.presentation.state.ValidationState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class CardNumberVerifierTest {
    private val verifier = CardNumberVerifier()

    private val validation = ValidationState(
        errorEmpty = "Campo obrigatório",
        errorIncomplete = "Número incompleto",
    )

    private fun state(
        length: Int,
        maxLength: Int = 16,
    ) = CardNumberState(
        length = length,
        maxLength = maxLength,
        validation = validation,
    )

    @Test
    fun `given length zero then returns errorEmpty`() {
        val result = verifier.verify(state(length = 0))

        assertEquals("Campo obrigatório", result)
    }

    @Test
    fun `given length one then returns errorIncomplete`() {
        val result = verifier.verify(state(length = 1))

        assertEquals("Número incompleto", result)
    }

    @Test
    fun `given length greater than zero and less than maxLength then returns errorIncomplete`() {
        val result = verifier.verify(state(length = 8, maxLength = 16))

        assertEquals("Número incompleto", result)
    }

    @Test
    fun `given length one less than maxLength then returns errorIncomplete`() {
        val result = verifier.verify(state(length = 15, maxLength = 16))

        assertEquals("Número incompleto", result)
    }

    @Test
    fun `given length equals maxLength then returns empty`() {
        val result = verifier.verify(state(length = 16, maxLength = 16))

        assertTrue(result.isEmpty())
    }

    @Test
    fun `given length equals maxLength for shorter card then returns empty`() {
        val result = verifier.verify(state(length = 13, maxLength = 13))

        assertTrue(result.isEmpty())
    }

    @Test
    fun `given length equals maxLength for longer card then returns empty`() {
        val result = verifier.verify(state(length = 19, maxLength = 19))

        assertTrue(result.isEmpty())
    }
}
