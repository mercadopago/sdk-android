package com.mercadopago.sdk.android.checkout.core.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class MPPaymentMethodConfigTest {
    @Test
    fun `given card with default args then exposes empty exclusions and default installment`() {
        val card = MPPaymentMethodConfig.Card()

        assertTrue(card.excludedPaymentTypes.isEmpty())
        assertTrue(card.excludedPaymentMethods.isEmpty())
        assertEquals(MPInstallment(), card.installment)
        assertTrue(card is MPPaymentMethodConfig)
    }

    @Test
    fun `given card with explicit args then exposes provided values`() {
        val card = MPPaymentMethodConfig.Card(
            excludedPaymentTypes = listOf(MPCardType.DEBIT),
            excludedPaymentMethods = listOf(MPCardBrand.Visa),
            installment = MPInstallment(minInstallments = 2, maxInstallments = 12),
        )

        assertEquals(listOf(MPCardType.DEBIT), card.excludedPaymentTypes)
        assertEquals(listOf(MPCardBrand.Visa), card.excludedPaymentMethods)
        assertEquals(MPInstallment(2, 12), card.installment)
    }

    @Test
    fun `given card when copy with null installment then equality changes`() {
        val card = MPPaymentMethodConfig.Card()

        val updated = card.copy(installment = null)

        assertNull(updated.installment)
        assertNotEquals(card, updated)
    }

    @Test
    fun `given defaults then contains card`() {
        val defaults = MPPaymentMethodConfig.defaults

        assertEquals(1, defaults.size)
        assertTrue(defaults.any { it is MPPaymentMethodConfig.Card })
    }

    @Test
    fun `given equal cards then they are equal with same hashCode and toString`() {
        val first = MPPaymentMethodConfig.Card()
        val second = MPPaymentMethodConfig.Card()

        assertEquals(first, second)
        assertEquals(first.hashCode(), second.hashCode())
        assertTrue(first.toString().isNotEmpty())
    }
}
