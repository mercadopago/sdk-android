package com.mercadopago.sdk.android.checkout.data.remote.mapper

import com.mercadopago.sdk.android.checkout.data.remote.response.OrderPaymentProcessedMethodResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.OrderPaymentProcessedResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.OrderProcessResponse
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class OrderProcessResponseMapperTest {
    @Test
    fun `given approved card response then maps statusDetail retryPossible totalAmount and paymentProcessed`() {
        val response = OrderProcessResponse(
            id = "ORD_1",
            status = "processed",
            statusDetail = "accredited",
            totalAmount = "150.50",
            retryPossible = false,
            paymentProcessed = OrderPaymentProcessedResponse(
                id = "PAY_1",
                status = "approved",
                statusDetail = "accredited",
                amount = "150.50",
                paymentMethod = OrderPaymentProcessedMethodResponse(
                    id = "master",
                    type = "credit_card",
                    installments = 3,
                    barcodeContent = null,
                    ticketUrl = null,
                    redirectUrl = null,
                ),
            ),
        )

        val output = response.toDomain()

        assertEquals("ORD_1", output.id)
        assertEquals("processed", output.status)
        assertEquals("accredited", output.statusDetail)
        assertEquals(BigDecimal("150.50"), output.totalAmount)
        assertEquals(false, output.retryPossible)
        assertEquals("PAY_1", output.paymentProcessed?.id)
        assertEquals("approved", output.paymentProcessed?.status)
        assertEquals("accredited", output.paymentProcessed?.statusDetail)
        assertEquals(BigDecimal("150.50"), output.paymentProcessed?.amount)
        assertEquals("master", output.paymentProcessed?.paymentMethod?.id)
        assertEquals("credit_card", output.paymentProcessed?.paymentMethod?.type)
        assertEquals(3, output.paymentProcessed?.paymentMethod?.installments)
    }

    @Test
    fun `given ticket action_required response then maps barcode ticketUrl and redirectUrl`() {
        val response = OrderProcessResponse(
            id = "ORD_2",
            status = "action_required",
            statusDetail = "waiting_payment",
            totalAmount = "80.00",
            retryPossible = false,
            paymentProcessed = OrderPaymentProcessedResponse(
                id = "PAY_2",
                status = "action_required",
                statusDetail = "waiting_payment",
                amount = "80.00",
                paymentMethod = OrderPaymentProcessedMethodResponse(
                    id = "rapipago",
                    type = "ticket",
                    installments = null,
                    barcodeContent = "1234567890",
                    ticketUrl = "https://mp/ticket/1",
                    redirectUrl = null,
                ),
            ),
        )

        val output = response.toDomain()

        assertEquals("1234567890", output.paymentProcessed?.paymentMethod?.barcodeContent)
        assertEquals("https://mp/ticket/1", output.paymentProcessed?.paymentMethod?.ticketUrl)
        assertNull(output.paymentProcessed?.paymentMethod?.redirectUrl)
    }

    @Test
    fun `given null id and status then maps to empty strings`() {
        val response = OrderProcessResponse(
            id = null,
            status = null,
            statusDetail = null,
            totalAmount = null,
            retryPossible = null,
            paymentProcessed = null,
        )

        val output = response.toDomain()

        assertEquals("", output.id)
        assertEquals("", output.status)
        assertNull(output.statusDetail)
        assertNull(output.totalAmount)
        assertNull(output.retryPossible)
        assertNull(output.paymentProcessed)
    }

    @Test
    fun `given malformed totalAmount then maps to null BigDecimal without throwing`() {
        val response = OrderProcessResponse(
            id = "ORD_3",
            status = "processed",
            statusDetail = null,
            totalAmount = "not-a-number",
            retryPossible = true,
            paymentProcessed = OrderPaymentProcessedResponse(
                id = "PAY_3",
                status = "approved",
                statusDetail = null,
                amount = "also-not-a-number",
                paymentMethod = null,
            ),
        )

        val output = response.toDomain()

        assertNull(output.totalAmount)
        assertNull(output.paymentProcessed?.amount)
    }

    @Test
    fun `given failed_card and waiting_ticket fixture then payment_processed exposes only the ticket`() {
        val response = OrderProcessResponse(
            id = "ORD_4",
            status = "action_required",
            statusDetail = "waiting_payment",
            totalAmount = "80.00",
            retryPossible = false,
            paymentProcessed = OrderPaymentProcessedResponse(
                id = "PAY_TICKET",
                status = "action_required",
                statusDetail = "waiting_payment",
                amount = "80.00",
                paymentMethod = OrderPaymentProcessedMethodResponse(
                    id = "pagofacil",
                    type = "ticket",
                    installments = null,
                    barcodeContent = "999888777",
                    ticketUrl = "https://mp/ticket/2",
                    redirectUrl = null,
                ),
            ),
        )

        val output = response.toDomain()

        assertEquals("PAY_TICKET", output.paymentProcessed?.id)
        assertEquals("pagofacil", output.paymentProcessed?.paymentMethod?.id)
        assertEquals("999888777", output.paymentProcessed?.paymentMethod?.barcodeContent)
    }
}
