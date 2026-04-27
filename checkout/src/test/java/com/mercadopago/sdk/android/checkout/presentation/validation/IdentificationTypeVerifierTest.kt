package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.sdk.android.checkout.presentation.state.IdentificationTypeState
import com.mercadopago.sdk.android.checkout.presentation.state.ValidationState
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class IdentificationTypeVerifierTest {
    private val verifier = IdentificationTypeVerifier()

    @Test
    fun `when value is empty then returns required field error`() {
        val state = IdentificationTypeState(value = "", validation = ValidationState(errorEmpty = "required"))

        val result = verifier.verify(state)

        assertEquals("required", result)
    }

    @Test
    fun `when value length is out of selected range then returns incomplete error`() {
        val selected = IdentificationType(minLength = 7, maxLength = 11)
        val state = IdentificationTypeState(
            value = "12345",
            selected = selected,
            validation = ValidationState(errorIncomplete = "required"),
        )

        val result = verifier.verify(state)

        assertEquals("required", result)
    }

    @Test
    fun `when value contains all zeros then returns all zeros error`() {
        val selected = IdentificationType(minLength = 7, maxLength = 11)
        val state = IdentificationTypeState(
            value = "00000000",
            selected = selected,
            validation = ValidationState(errorInvalid = "all zeros"),
        )

        val result = verifier.verify(state)

        assertEquals("all zeros", result)
    }

    @Test
    fun `when selected is null then incomplete check is skipped`() {
        val result = verifier.verify(IdentificationTypeState(value = "123", selected = null))

        assertTrue(result.isEmpty())
    }

    @Test
    fun `when value is valid then returns empty string`() {
        val selected = IdentificationType(minLength = 7, maxLength = 11)

        val result = verifier.verify(IdentificationTypeState(value = "12345678", selected = selected))

        assertTrue(result.isEmpty())
    }
}
