package com.mercadopago.sdk.android.checkout.domain.model

import com.mercadopago.sdk.android.checkout.core.model.MPOrder
import com.mercadopago.sdk.android.checkout.domain.model.params.PaymentMethodSource
import org.junit.Assert.assertEquals
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

    @Test
    fun `given a saved card when building the order params then the payment method source is saved_card`() {
        val method = PaymentMethodOutput(
            type = "credit_card",
            title = "Visa",
            cardData = CardDataOutput(
                id = "card-1",
                bin = "450995",
                lastFourDigits = "1234",
                paymentMethodId = "visa",
                paymentTypeId = "credit_card",
                issuerId = 1,
                securityCode = SecurityCodeOutput(length = 3, screen = null),
            ),
        )

        val params = method.toProcessOrderParams(
            order = MPOrder(orderId = "ORD_1", clientToken = "client-token"),
            checkoutType = "payment",
        )

        assertEquals(PaymentMethodSource.SavedCard, params.paymentMethodSource)
        assertEquals("card-1", params.cardId)
    }
}
