package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.android.sdk.checkout.R
import com.mercadopago.sdk.android.checkout.domain.provider.StringProvider
import com.mercadopago.sdk.android.checkout.presentation.state.IdentificationTypeState
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class IdentificationTypeVerifierTest {
    private val stringProvider: StringProvider = mockk()
    private val verifier = IdentificationTypeVerifier(stringProvider)

    @Test
    fun `when value is empty then returns required field error`() {
        every { stringProvider.getString(R.string.card_form_error_required_field) } returns "required"

        val result = verifier.verify(IdentificationTypeState(value = ""))

        assertEquals("required", result)
    }

    @Test
    fun `when value length is out of selected range then returns required field error`() {
        every { stringProvider.getString(R.string.card_form_error_required_field) } returns "required"
        val selected = IdentificationType(minLength = 7, maxLength = 11)

        val result = verifier.verify(IdentificationTypeState(value = "12345", selected = selected))

        assertEquals("required", result)
    }

    @Test
    fun `when value contains all zeros then returns all zeros error`() {
        every { stringProvider.getString(R.string.card_form_error_document_all_zeros) } returns "all zeros"
        val selected = IdentificationType(minLength = 7, maxLength = 11)

        val result = verifier.verify(IdentificationTypeState(value = "00000000", selected = selected))

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
