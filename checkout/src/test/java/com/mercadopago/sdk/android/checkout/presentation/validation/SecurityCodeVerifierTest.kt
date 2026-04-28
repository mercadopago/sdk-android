package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeState
import com.mercadopago.sdk.android.checkout.presentation.state.ValidationState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class SecurityCodeVerifierTest {
    private val verifier = SecurityCodeVerifier()

    private val validation = ValidationState(
        errorEmpty = "Campo obrigatório",
        errorIncomplete = "CVV incompleto",
    )

    private fun state(
        length: Int,
        maxLength: Int = 3,
    ) = SecurityCodeState(
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

        assertEquals("CVV incompleto", result)
    }

    @Test
    fun `given length greater than zero and less than maxLength then returns errorIncomplete`() {
        val result = verifier.verify(state(length = 2, maxLength = 3))

        assertEquals("CVV incompleto", result)
    }

    @Test
    fun `given length one less than maxLength then returns errorIncomplete`() {
        val result = verifier.verify(state(length = 3, maxLength = 4))

        assertEquals("CVV incompleto", result)
    }

    @Test
    fun `given length equals maxLength for 3-digit CVV then returns empty`() {
        val result = verifier.verify(state(length = 3, maxLength = 3))

        assertTrue(result.isEmpty())
    }

    @Test
    fun `given length equals maxLength for 4-digit CVV then returns empty`() {
        val result = verifier.verify(state(length = 4, maxLength = 4))

        assertTrue(result.isEmpty())
    }
}
