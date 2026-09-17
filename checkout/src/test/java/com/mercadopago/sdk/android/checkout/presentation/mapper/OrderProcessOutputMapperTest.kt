package com.mercadopago.sdk.android.checkout.presentation.mapper

import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals

internal class OrderProcessOutputMapperTest {
    @Test
    fun `given approved card payment_processed then maps every field from it`() {
        val output = OrderProcessOutput(
            id = "ORD_1",
            status = "processed",
            statusDetail = "accredited",
            totalAmount = BigDecimal("100.00"),
            retryPossible = false,
            paymentProcessed = OrderProcessOutput.PaymentProcessed(
                id = "PAY_1",
                status = "approved",
                statusDetail = "accredited",
                amount = BigDecimal("100.00"),
                paymentMethod = OrderProcessOutput.PaymentProcessedMethod(
                    id = "visa",
                    type = "credit_card",
                    installments = 3,
                    barcodeContent = null,
                    ticketUrl = null,
                    redirectUrl = null,
                ),
            ),
        )

        val payment = output.toPayment()

        assertEquals("ORD_1", payment.orderId)
        assertEquals("processed", payment.orderStatus)
        assertEquals("visa", payment.paymentMethodId)
        assertEquals("credit_card", payment.paymentTypeId)
        assertEquals("accredited", payment.orderStatusDetail)
        assertEquals(BigDecimal("100.00"), payment.transactionAmount)
    }

    @Test
    fun `given ticket action_required payment_processed then maps ticket payment method and amount`() {
        val output = OrderProcessOutput(
            id = "ORD_2",
            status = "action_required",
            statusDetail = "waiting_payment",
            totalAmount = BigDecimal("50.00"),
            retryPossible = false,
            paymentProcessed = OrderProcessOutput.PaymentProcessed(
                id = "PAY_2",
                status = "action_required",
                statusDetail = "waiting_payment",
                amount = BigDecimal("50.00"),
                paymentMethod = OrderProcessOutput.PaymentProcessedMethod(
                    id = "rapipago",
                    type = "ticket",
                    installments = null,
                    barcodeContent = "1234567890",
                    ticketUrl = "https://example.com/ticket",
                    redirectUrl = "https://example.com/redirect",
                ),
            ),
        )

        val payment = output.toPayment()

        assertEquals("rapipago", payment.paymentMethodId)
        assertEquals("ticket", payment.paymentTypeId)
        assertEquals("waiting_payment", payment.orderStatusDetail)
        assertEquals(BigDecimal("50.00"), payment.transactionAmount)
    }

    @Test
    fun `given no payment_processed then payment-specific fields are null but order fields are preserved`() {
        val output = OrderProcessOutput(
            id = "ORD_3",
            status = "failed",
            statusDetail = "cc_rejected_bad_filled_card_number",
            totalAmount = BigDecimal("20.00"),
            retryPossible = true,
            paymentProcessed = null,
        )

        val payment = output.toPayment()

        assertEquals("ORD_3", payment.orderId)
        assertEquals("failed", payment.orderStatus)
        assertEquals("cc_rejected_bad_filled_card_number", payment.orderStatusDetail)
        assertEquals("", payment.paymentMethodId)
        assertEquals("", payment.paymentTypeId)
        assertEquals(BigDecimal("20.00"), payment.transactionAmount)
    }
}
