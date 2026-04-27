package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeState
import com.mercadopago.sdk.android.checkout.presentation.state.ValidationState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class SecurityCodeVerifierTest {
    private val verifier = SecurityCodeVerifier()

    @Test
    fun `when length is 0 then returns required field error`() {
        val state = SecurityCodeState(
            length = 0,
            maxLength = 3,
            validation = ValidationState(errorEmpty = "required"),
        )

        val result = verifier.verify(state)

        assertEquals("required", result)
    }

    @Test
    fun `when length greater than 0 and less than maxLength then returns incomplete error`() {
        val state = SecurityCodeState(
            length = 2,
            maxLength = 3,
            validation = ValidationState(errorIncomplete = "incomplete"),
        )

        val result = verifier.verify(state)

        assertEquals("incomplete", result)
    }

    @Test
    fun `when length equals maxLength then returns empty string`() {
        val result = verifier.verify(SecurityCodeState(length = 3, maxLength = 3))

        assertTrue(result.isEmpty())
    }
}
