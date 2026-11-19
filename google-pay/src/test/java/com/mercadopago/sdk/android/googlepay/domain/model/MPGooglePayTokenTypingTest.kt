package com.mercadopago.sdk.android.googlepay.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

internal class MPGooglePayTokenTypingTest {
    // Compiles only if the callback result carries MPGooglePayToken in Success.
    private fun successToken(
        result: MPGooglePayResult<MPGooglePayToken>,
    ): MPGooglePayToken? =
        (result as? MPGooglePayResult.Success)?.paymentData

    @Test
    fun `success of token mode exposes MPGooglePayToken`() {
        val token = MPGooglePayToken("tok_1", "411111", "visa", MPGooglePayPaymentType.DEBIT_CARD, null)

        assertEquals(token, successToken(MPGooglePayResult.Success(token)))
    }

    @Test
    fun `token toString does not leak token id, bin nor card description`() {
        val token = MPGooglePayToken(
            "tok_secret",
            "411111",
            "visa",
            MPGooglePayPaymentType.CREDIT_CARD,
            "Visa •••• 1111",
        )

        val text = token.toString()

        assertFalse(text.contains("tok_secret"))
        assertFalse(text.contains("411111"))
        assertFalse(text.contains("Visa •••• 1111"))
        assertEquals("MPGooglePayToken(paymentMethodId=visa, paymentTypeId=CREDIT_CARD)", text)
        assertFalse(MPGooglePayResult.Success(token).toString().contains("Visa •••• 1111"))
        assertFalse(MPGooglePayResult.Success(token).toString().contains("tok_secret"))
    }
}
