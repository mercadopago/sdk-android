package com.mercadopago.sdk.android.checkout.presentation.state

import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class PaymentBrickViewEventTest {
    private val params = ProcessOrderParams(
        orderId = "ORD_001",
        clientToken = "token-abc",
        paymentMethodId = "visa",
        paymentMethodType = "credit_card",
        token = "card_token",
        installments = 3,
        amount = "100.00",
        bin = "453998",
    )

    @Test
    fun `given OnPaymentReadyForReview then is a PaymentBrickViewEvent`() {
        // Given / When
        val event = PaymentBrickViewEvent.OnPaymentReadyForReview(params = params)

        // Then
        assertTrue(event is PaymentBrickViewEvent)
    }

    @Test
    fun `given OnPaymentReadyForReview then exposes params`() {
        // Given / When
        val event = PaymentBrickViewEvent.OnPaymentReadyForReview(params = params)

        // Then
        assertEquals(params, event.params)
    }

    @Test
    fun `given OnPaymentReadyForReview then exposes bin from params`() {
        // Given / When
        val event = PaymentBrickViewEvent.OnPaymentReadyForReview(params = params)

        // Then
        assertEquals("453998", event.params.bin)
    }
}
