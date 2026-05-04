package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.android.sdk.checkout.R
import com.mercadopago.sdk.android.checkout.domain.provider.StringProvider
import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberState
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class CardNumberVerifierTest {
    private val stringProvider: StringProvider = mockk()
    private val verifier = CardNumberVerifier(stringProvider)

    @Test
    fun `when length is 0 then returns required field error`() {
        every { stringProvider.getString(R.string.card_form_error_required_field) } returns "required"

        val result = verifier.verify(CardNumberState(length = 0, maxLength = 16))

        assertEquals("required", result)
    }

    @Test
    fun `when length greater than 0 and less than maxLength then returns incomplete error`() {
        every { stringProvider.getString(R.string.card_form_error_card_number_incomplete) } returns "incomplete"

        val result = verifier.verify(CardNumberState(length = 8, maxLength = 16))

        assertEquals("incomplete", result)
    }

    @Test
    fun `when length equals maxLength then returns empty string`() {
        val result = verifier.verify(CardNumberState(length = 16, maxLength = 16))

        assertTrue(result.isEmpty())
    }
}
