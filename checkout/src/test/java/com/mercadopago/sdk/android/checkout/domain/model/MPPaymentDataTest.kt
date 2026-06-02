package com.mercadopago.sdk.android.checkout.domain.model

import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

internal class MPPaymentDataTest {
    private val payer = Payer(documentType = "CPF", documentNumber = "12345678909")

    @Test
    fun `given card save data then exposes all fields`() {
        val data = MPPaymentData.CardSave(
            token = "tok_123",
            paymentMethodId = "visa",
            paymentTypeId = "credit_card",
            payer = payer,
            issuerId = "25",
        )

        assertEquals("tok_123", data.token)
        assertEquals("visa", data.paymentMethodId)
        assertEquals("credit_card", data.paymentTypeId)
        assertEquals(payer, data.payer)
        assertEquals("25", data.issuerId)
        assertTrue(data is MPPaymentData)
    }

    @Test
    fun `given card save data when copy with new token then other fields are kept`() {
        val data = MPPaymentData.CardSave(
            token = "tok_123",
            paymentMethodId = "visa",
            paymentTypeId = "credit_card",
            payer = payer,
            issuerId = "25",
        )

        val updated = data.copy(token = "tok_456")

        assertEquals("tok_456", updated.token)
        assertEquals(data.paymentMethodId, updated.paymentMethodId)
        assertNotEquals(data, updated)
        assertEquals(data.hashCode(), data.copy().hashCode())
    }

    @Test
    fun `given card transaction data then exposes all fields`() {
        val data = MPPaymentData.CardTransaction(
            transactionAmount = BigDecimal("188000.00"),
            paymentMethodId = "master",
            paymentTypeId = "credit_card",
            payer = payer,
            installment = 6,
            issuerId = "310",
        )

        assertEquals(BigDecimal("188000.00"), data.transactionAmount)
        assertEquals("master", data.paymentMethodId)
        assertEquals("credit_card", data.paymentTypeId)
        assertEquals(payer, data.payer)
        assertEquals(6, data.installment)
        assertEquals("310", data.issuerId)
    }

    @Test
    fun `given card transaction data when copy with new installment then equality changes`() {
        val data = MPPaymentData.CardTransaction(
            transactionAmount = BigDecimal("100.00"),
            paymentMethodId = "master",
            paymentTypeId = "credit_card",
            payer = payer,
            installment = 1,
            issuerId = "310",
        )

        val updated = data.copy(installment = 12)

        assertEquals(12, updated.installment)
        assertNotEquals(data, updated)
        assertTrue(data.toString().contains("CardTransaction"))
    }
}
