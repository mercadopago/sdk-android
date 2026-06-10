package com.mercadopago.sdk.android.checkout.core.model

import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

internal class MPModelsTest {
    @Test
    fun `given installment with default args then uses default min and max`() {
        val installment = MPInstallment()

        assertEquals(DEFAULT_INSTALLMENT_MIN, installment.minInstallments)
        assertEquals(DEFAULT_INSTALLMENT_MAX, installment.maxInstallments)
    }

    @Test
    fun `given installment with explicit args then exposes provided values`() {
        val installment = MPInstallment(minInstallments = 3, maxInstallments = 24)

        assertEquals(3, installment.minInstallments)
        assertEquals(24, installment.maxInstallments)
    }

    @Test
    fun `given installment when copy with new max then equality changes`() {
        val installment = MPInstallment()

        val updated = installment.copy(maxInstallments = 99)

        assertEquals(99, updated.maxInstallments)
        assertNotEquals(installment, updated)
    }

    @Test
    fun `given payer then exposes email`() {
        val payer = MPPayer(email = "buyer@test.com")

        assertEquals("buyer@test.com", payer.email)
        assertEquals(MPPayer("buyer@test.com"), payer)
    }

    @Test
    fun `given payer when copy with new email then equality changes`() {
        val payer = MPPayer(email = "a@test.com")

        val updated = payer.copy(email = "b@test.com")

        assertEquals("b@test.com", updated.email)
        assertNotEquals(payer, updated)
    }

    @Test
    fun `given order then exposes amount and payer`() {
        val payer = MPPayer(email = "buyer@test.com")
        val order = MPOrder(amount = BigDecimal("150.50"), payer = payer)

        assertEquals(BigDecimal("150.50"), order.amount)
        assertEquals(payer, order.payer)
    }

    @Test
    fun `given order when copy with new amount then equality changes`() {
        val order = MPOrder(amount = BigDecimal("100.00"), payer = MPPayer("buyer@test.com"))

        val updated = order.copy(amount = BigDecimal("200.00"))

        assertEquals(BigDecimal("200.00"), updated.amount)
        assertNotEquals(order, updated)
    }

    @Test
    fun `given equal installments then they are equal with same hashCode and toString`() {
        val first = MPInstallment(minInstallments = 2, maxInstallments = 12)
        val second = MPInstallment(minInstallments = 2, maxInstallments = 12)

        assertEquals(first, second)
        assertEquals(first.hashCode(), second.hashCode())
        assertTrue(first.toString().isNotEmpty())
    }

    @Test
    fun `given equal payers then they are equal with same hashCode and toString`() {
        val first = MPPayer(email = "buyer@test.com")
        val second = MPPayer(email = "buyer@test.com")

        assertEquals(first, second)
        assertEquals(first.hashCode(), second.hashCode())
        assertTrue(first.toString().contains("buyer@test.com"))
    }

    @Test
    fun `given equal orders then they are equal with same hashCode and toString`() {
        val payer = MPPayer(email = "buyer@test.com")
        val first = MPOrder(amount = BigDecimal("150.50"), payer = payer)
        val second = MPOrder(amount = BigDecimal("150.50"), payer = payer)

        assertEquals(first, second)
        assertEquals(first.hashCode(), second.hashCode())
        assertTrue(first.toString().isNotEmpty())
    }

    @Test
    fun `given order when copy with new payer then equality changes`() {
        val order = MPOrder(amount = BigDecimal("100.00"), payer = MPPayer("a@test.com"))

        val updated = order.copy(payer = MPPayer("b@test.com"))

        assertEquals(MPPayer("b@test.com"), updated.payer)
        assertNotEquals(order, updated)
    }
}
