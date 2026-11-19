package com.mercadopago.sdk.android.googlepay.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

internal class MPGooglePayTypeTest {
    @Test
    fun `token type keeps the transaction`() {
        val transaction = MPGooglePayTransaction(totalPrice = "10.00", currencyCode = "BRL")

        val type: MPGooglePayType<MPGooglePayToken> = MPGooglePayType.Token(transaction)

        assertEquals(transaction, (type as MPGooglePayType.Token).transaction)
    }
}
