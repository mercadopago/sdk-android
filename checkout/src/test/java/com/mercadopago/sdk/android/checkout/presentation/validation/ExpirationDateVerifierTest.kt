package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.sdk.android.checkout.presentation.state.ExpirationDateState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class ExpirationDateVerifierTest {
    private val verifier = ExpirationDateVerifier()

    @Test
    fun `when length is 0 then returns errorEmptyField from state`() {
        val state = ExpirationDateState(
            length = 0,
            errorEmptyField = "Campo obrigatório",
        )
        val result = verifier.verify(state)

        assertEquals("Campo obrigatório", result)
    }

    @Test
    fun `when length greater than 0 and not filled then returns errorIncompleteField from state`() {
        val state = ExpirationDateState(
            length = 2,
            filled = false,
            errorIncompleteField = "Data incompleta",
        )
        val result = verifier.verify(state)

        assertEquals("Data incompleta", result)
    }

    @Test
    fun `when filled and isValid is false then returns errorInvalidField from state`() {
        val state = ExpirationDateState(
            length = 4,
            filled = true,
            isValid = false,
            errorInvalidField = "Data inválida",
        )
        val result = verifier.verify(state)

        assertEquals("Data inválida", result)
    }

    @Test
    fun `when filled and isValid is true then returns empty string`() {
        val state = ExpirationDateState(
            length = 4,
            filled = true,
            isValid = true,
        )
        val result = verifier.verify(state)

        assertTrue(result.isEmpty())
    }
}
