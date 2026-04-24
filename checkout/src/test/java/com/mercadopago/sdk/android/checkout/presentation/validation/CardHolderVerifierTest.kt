package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.android.sdk.checkout.R
import com.mercadopago.sdk.android.checkout.domain.provider.StringProvider
import com.mercadopago.sdk.android.checkout.presentation.state.CardHolderState
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class CardHolderVerifierTest {
    private val stringProvider: StringProvider = mockk()
    private val verifier = CardHolderVerifier(stringProvider)

    @Test
    fun `when value is empty then returns required field error`() {
        every { stringProvider.getString(R.string.card_form_error_required_field) } returns "required"

        val result = verifier.verify(CardHolderState(value = ""))

        assertEquals("required", result)
    }

    @Test
    fun `when value length is less than min characters then returns incomplete error`() {
        every { stringProvider.getString(R.string.card_form_error_cardholder_incomplete) } returns "incomplete"

        val result = verifier.verify(CardHolderState(value = "AB"))

        assertEquals("incomplete", result)
    }

    @Test
    fun `when value contains special characters then returns format error`() {
        every { stringProvider.getString(R.string.card_form_error_cardholder_format) } returns "invalid format"

        val result = verifier.verify(CardHolderState(value = "AB@"))

        assertEquals("invalid format", result)
    }

    @Test
    fun `when value is valid then returns empty string`() {
        val result = verifier.verify(CardHolderState(value = "John Doe"))

        assertTrue(result.isEmpty())
    }
}
