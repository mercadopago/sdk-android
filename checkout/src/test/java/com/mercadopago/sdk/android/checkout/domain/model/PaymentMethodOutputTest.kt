package com.mercadopago.sdk.android.checkout.domain.model

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

internal class PaymentMethodOutputTest {
    @Test
    fun `given type ticket when isTicket then returns true`() {
        val method = PaymentMethodOutput(type = "ticket", title = "Boleto")
        assertTrue(method.isTicket)
    }

    @Test
    fun `given type other than ticket when isTicket then returns false`() {
        val method = PaymentMethodOutput(type = "new_card", title = "Card")
        assertFalse(method.isTicket)
    }
}
