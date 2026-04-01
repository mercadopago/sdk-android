package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.android.sdk.checkout.R
import com.mercadopago.sdk.android.checkout.domain.provider.StringProvider
import com.mercadopago.sdk.android.checkout.presentation.state.ExpirationDateState
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class ExpirationDateVerifierTest {
    private val stringProvider: StringProvider = mockk()
    private val verifier = ExpirationDateVerifier(stringProvider)

    @Test
    fun `when length is 0 then returns required field error`() {
        every { stringProvider.getString(R.string.card_form_error_required_field) } returns "required"

        val state = ExpirationDateState(length = 0)
        val result = verifier.verify(state)

        assertEquals("required", result)
    }

    @Test
    fun `when length greater than 0 and not filled then returns incomplete error`() {
        every { stringProvider.getString(R.string.card_form_error_expiration_incomplete) } returns "incomplete"

        val state = ExpirationDateState(length = 2, filled = false)
        val result = verifier.verify(state)

        assertEquals("incomplete", result)
    }

    @Test
    fun `when filled and isValid is false then returns invalid date error`() {
        every { stringProvider.getString(R.string.card_form_error_expiration_invalid) } returns "invalid date"

        val state = ExpirationDateState(length = 4, filled = true, isValid = false)
        val result = verifier.verify(state)

        assertEquals("invalid date", result)
    }

    @Test
    fun `when filled and isValid is true then returns empty string`() {
        val state = ExpirationDateState(length = 4, filled = true, isValid = true)
        val result = verifier.verify(state)

        assertTrue(result.isEmpty())
    }
}
