package com.mercadopago.sdk.android.checkout.presentation.mapper

import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPOrder
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

private val resolvePaymentDataOrder = MPOrder(orderId = "ORD_1", clientToken = "token")

private val resolvePaymentDataParams = ProcessOrderParams(
    orderId = "ORD_1",
    clientToken = "token",
    paymentMethodId = "stale_method_id",
    paymentMethodType = "stale_method_type",
    token = "tkn",
    installments = 1,
)

private val resolvePaymentDataOutput = OrderProcessOutput(
    id = "ORD_1",
    status = "approved",
    paymentProcessed = OrderProcessOutput.PaymentProcessed(
        id = "PAY_1",
        status = "approved",
        statusDetail = null,
        amount = null,
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

internal class OrderProcessOutputMapperTest {
    @Test
    fun `given approved card payment_processed then maps every field from it`() {
        val output = OrderProcessOutput(
            id = "ORD_1",
            status = "processed",
            statusDetail = "accredited",
            totalAmount = BigDecimal("100.00"),
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
    }

    @Test
    fun `given ticket action_required payment_processed then maps ticket payment method and amount`() {
        val output = OrderProcessOutput(
            id = "ORD_2",
            status = "action_required",
            statusDetail = "waiting_payment",
            totalAmount = BigDecimal("50.00"),
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
    }

    @Test
    fun `given no payment_processed then payment-specific fields are null but order fields are preserved`() {
        val output = OrderProcessOutput(
            id = "ORD_3",
            status = "failed",
            statusDetail = "cc_rejected_bad_filled_card_number",
            totalAmount = BigDecimal("20.00"),
            paymentProcessed = null,
        )

        val payment = output.toPayment()

        assertEquals("ORD_3", payment.orderId)
        assertEquals("failed", payment.orderStatus)
        assertEquals("cc_rejected_bad_filled_card_number", payment.orderStatusDetail)
        assertEquals("", payment.paymentMethodId)
        assertEquals("", payment.paymentTypeId)
    }

    @Test
    fun `given card transaction checkout type then resolvePaymentData builds CardTransaction from params`() {
        val checkoutConfiguration = CheckoutConfiguration(
            checkoutType = MPCheckoutType.CardTransaction(order = resolvePaymentDataOrder),
            paymentMethodConfigs = emptyList(),
        )

        val result = resolvePaymentDataOutput.resolvePaymentData(checkoutConfiguration, resolvePaymentDataParams)

        assertIs<MPPaymentData.CardTransaction>(result)
        assertEquals(resolvePaymentDataParams.paymentMethodId, result.paymentMethodId)
        assertEquals(resolvePaymentDataParams.paymentMethodType, result.paymentTypeId)
    }

    @Test
    fun `given payment checkout type then resolvePaymentData delegates to the payment_processed projection`() {
        val checkoutConfiguration = CheckoutConfiguration(
            checkoutType = MPCheckoutType.Payment(order = resolvePaymentDataOrder),
            paymentMethodConfigs = emptyList(),
        )

        val result = resolvePaymentDataOutput.resolvePaymentData(checkoutConfiguration, resolvePaymentDataParams)

        assertEquals(resolvePaymentDataOutput.toPayment(), result)
        assertIs<MPPaymentData.Payment>(result)
        assertEquals("visa", result.paymentMethodId)
        assertEquals("credit_card", result.paymentTypeId)
    }

    @Test
    fun `given null checkout configuration then resolvePaymentData delegates to the payment_processed projection`() {
        val result = resolvePaymentDataOutput.resolvePaymentData(null, resolvePaymentDataParams)

        assertEquals(resolvePaymentDataOutput.toPayment(), result)
    }
}
