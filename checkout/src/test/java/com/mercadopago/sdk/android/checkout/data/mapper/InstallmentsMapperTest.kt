package com.mercadopago.sdk.android.checkout.data.mapper

import com.mercadopago.sdk.android.checkout.presentation.extensions.toCurrencyString
import com.mercadopago.sdk.android.coremethods.domain.model.Installment
import com.mercadopago.sdk.android.coremethods.domain.model.PayerCost
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class InstallmentsMapperTest {
    @Test
    fun `given null installment then returns empty list`() {
        val result = null.toInstallmentsState()

        assertEquals(emptyList(), result)
    }

    @Test
    fun `given installment with null payerCost then returns empty list`() {
        val installment = Installment(payerCost = null)

        val result = installment.toInstallmentsState()

        assertEquals(emptyList(), result)
    }

    @Test
    fun `given installment with empty payerCost then returns empty list`() {
        val installment = Installment(payerCost = emptyList())

        val result = installment.toInstallmentsState()

        assertEquals(emptyList(), result)
    }

    @Test
    fun `given installment with one payerCost then returns one InstallmentState`() {
        val installment = Installment(payerCost = listOf(PayerCost(instalments = 3)))

        val result = installment.toInstallmentsState()

        assertEquals(1, result.size)
    }

    @Test
    fun `given installment with multiple payerCosts then returns correct count`() {
        val installment = Installment(
            payerCost = listOf(
                PayerCost(instalments = 1),
                PayerCost(instalments = 3),
                PayerCost(instalments = 6),
            ),
        )

        val result = installment.toInstallmentsState()

        assertEquals(3, result.size)
    }

    @Test
    fun `given payerCost then text contains separator and installment count`() {
        val installment = Installment(payerCost = listOf(PayerCost(instalments = 6)))

        val result = installment.toInstallmentsState()

        assertTrue(result.first().text.contains("6"))
        assertTrue(result.first().text.contains(SEPARATOR_INSTALLMENTS))
    }

    @Test
    fun `given payerCost then description is empty`() {
        val installment = Installment(payerCost = listOf(PayerCost(instalments = 3)))

        val result = installment.toInstallmentsState()

        assertEquals(EMPTY, result.first().description)
    }

    @Test
    fun `given payerCost then isSelected is false`() {
        val installment = Installment(payerCost = listOf(PayerCost(instalments = 3)))

        val result = installment.toInstallmentsState()

        assertFalse(result.first().isSelected)
    }

    @Test
    fun `given payerCost number is set from instalments field`() {
        val installment = Installment(payerCost = listOf(PayerCost(instalments = 12)))

        val result = installment.toInstallmentsState()

        assertEquals(12, result.first().number)
    }

    @Test
    fun `given payerCost with null instalments then number defaults to FIRST_INSTALLMENT`() {
        val installment = Installment(payerCost = listOf(PayerCost(instalments = null)))

        val result = installment.toInstallmentsState()

        assertEquals(FIRST_INSTALLMENT, result.first().number)
    }

    @Test
    fun `given installmentAmount equals totalAmount then interestFree is true`() {
        val installment = Installment(
            payerCost = listOf(PayerCost(instalments = 3, installmentAmount = 100f, totalAmount = 100f)),
        )

        val result = installment.toInstallmentsState()

        assertTrue(result.first().interestFree)
    }

    @Test
    fun `given installmentAmount differs from totalAmount then interestFree is false`() {
        val installment = Installment(
            payerCost = listOf(PayerCost(instalments = 3, installmentAmount = 100f, totalAmount = 310f)),
        )

        val result = installment.toInstallmentsState()

        assertFalse(result.first().interestFree)
    }

    @Test
    fun `given instalments is 1 then trailing is empty`() {
        val payerCost = PayerCost(instalments = FIRST_INSTALLMENT, installmentAmount = 100f, totalAmount = 100f)

        val result = payerCost.formatTrailingText()

        assertEquals(EMPTY, result)
    }

    @Test
    fun `given installmentAmount equals totalAmount and more than 1 installment then trailing is interest free`() {
        val payerCost = PayerCost(instalments = 3, installmentAmount = 100f, totalAmount = 100f)

        val result = payerCost.formatTrailingText()

        assertEquals(INTEREST_FREE, result)
    }

    @Test
    fun `given installmentAmount differs from totalAmount then trailing is formatted total amount`() {
        val totalAmount = 310f
        val payerCost = PayerCost(instalments = 3, installmentAmount = 100f, totalAmount = totalAmount)

        val result = payerCost.formatTrailingText()

        assertEquals(totalAmount.toCurrencyString(), result)
    }

    @Test
    fun `given totalAmount is null then trailing is empty string`() {
        val payerCost = PayerCost(instalments = 3, installmentAmount = 100f, totalAmount = null)

        val result = payerCost.formatTrailingText()

        assertEquals(EMPTY, result)
    }
}
