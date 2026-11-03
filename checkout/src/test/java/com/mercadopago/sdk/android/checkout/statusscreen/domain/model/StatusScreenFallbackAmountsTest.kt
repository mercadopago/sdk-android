package com.mercadopago.sdk.android.checkout.statusscreen.domain.model

import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class StatusScreenFallbackAmountsTest {
    private fun payment(
        amount: String? = null,
        totalPaidAmount: String? = null,
        orderTotalAmount: String? = null,
        installments: Int? = null,
        methodType: String? = "credit_card",
    ) = OrderProcessOutput.PaymentProcessed(
        id = "PAY_1",
        status = "processed",
        statusDetail = null,
        amount = amount?.let(::BigDecimal),
        paymentMethod = OrderProcessOutput.PaymentProcessedMethod(
            id = "master",
            type = methodType,
            installments = installments,
            barcodeContent = null,
            ticketUrl = null,
            redirectUrl = null,
        ),
        totalPaidAmount = totalPaidAmount?.let(::BigDecimal),
        orderTotalAmount = orderTotalAmount?.let(::BigDecimal),
    )

    @Test
    fun `given all amounts then the paid amount is the total paid amount`() {
        // Given
        val payment = payment(amount = "1000.00", totalPaidAmount = "1167.50", orderTotalAmount = "900.00")

        // When
        val paid = payment.paidAmount()

        // Then
        assertEquals(BigDecimal("1167.50"), paid)
    }

    @Test
    fun `given no total paid amount then the paid amount falls back to the payment amount and then the order total`() {
        // Given
        val withAmount = payment(amount = "1000.00", orderTotalAmount = "900.00")
        val onlyOrderTotal = payment(orderTotalAmount = "900.00")

        // When
        val paidWithAmount = withAmount.paidAmount()
        val paidOnlyOrderTotal = onlyOrderTotal.paidAmount()

        // Then
        assertEquals(BigDecimal("1000.00"), paidWithAmount)
        assertEquals(BigDecimal("900.00"), paidOnlyOrderTotal)
    }

    @Test
    fun `given an installment payment with interest then the installments carry the split amount`() {
        // Given
        val payment = payment(totalPaidAmount = "1167.50", orderTotalAmount = "1000.00", installments = 2)

        // When
        val installments = payment.fallbackInstallments()

        // Then
        assertNotNull(installments)
        assertEquals(2, installments.count)
        assertEquals(BigDecimal("583.75"), installments.perInstallment)
        assertFalse(installments.interestFree)
    }

    @Test
    fun `given an installment payment that does not exceed the order total then it is interest free`() {
        // Given
        val payment = payment(totalPaidAmount = "1000.00", orderTotalAmount = "1000.00", installments = 4)

        // When
        val installments = payment.fallbackInstallments()

        // Then
        assertNotNull(installments)
        assertEquals(BigDecimal("250.00"), installments.perInstallment)
        assertTrue(installments.interestFree)
    }

    @Test
    fun `given a single installment then there are no installments to show`() {
        // Given
        val payment = payment(totalPaidAmount = "1000.00", orderTotalAmount = "1000.00", installments = 1)

        // When
        val installments = payment.fallbackInstallments()

        // Then
        assertNull(installments)
    }

    @Test
    fun `given a debit card then there are no installments to show`() {
        // Given
        val payment = payment(
            totalPaidAmount = "1000.00",
            orderTotalAmount = "1000.00",
            installments = 3,
            methodType = "debit_card",
        )

        // When
        val installments = payment.fallbackInstallments()

        // Then
        assertNull(installments)
    }

    @Test
    fun `given installments without a total paid amount then there are no installments to show`() {
        // Given
        val payment = payment(amount = "1000.00", orderTotalAmount = "1000.00", installments = 3)

        // When
        val installments = payment.fallbackInstallments()

        // Then
        assertNull(installments)
    }

    @Test
    fun `given a payment amount then the ticket amount is the payment amount`() {
        // Given
        val payment = payment(amount = "900.00", orderTotalAmount = "1000.00")

        // When
        val amount = payment.ticketAmount()

        // Then
        assertEquals(BigDecimal("900.00"), amount)
    }

    @Test
    fun `given no payment amount then the ticket amount falls back to the order total`() {
        // Given
        val payment = payment(orderTotalAmount = "1000.00")

        // When
        val amount = payment.ticketAmount()

        // Then
        assertEquals(BigDecimal("1000.00"), amount)
    }

    @Test
    fun `given a method id with underscores then the display name is capitalized and readable`() {
        // Given
        val payment = payment().copy(
            paymentMethod = OrderProcessOutput.PaymentProcessedMethod(
                id = "account_money",
                type = null,
                installments = null,
                barcodeContent = null,
                ticketUrl = null,
                redirectUrl = null,
            ),
        )

        // When
        val name = payment.methodDisplayName()

        // Then
        assertEquals("Account money", name)
    }

    @Test
    fun `given the master method id then the display name is Mastercard`() {
        // Given
        val payment = payment().copy(
            paymentMethod = OrderProcessOutput.PaymentProcessedMethod(
                id = "master",
                type = null,
                installments = null,
                barcodeContent = null,
                ticketUrl = null,
                redirectUrl = null,
            ),
        )

        // When
        val name = payment.methodDisplayName()

        // Then
        assertEquals("Mastercard", name)
    }
}
