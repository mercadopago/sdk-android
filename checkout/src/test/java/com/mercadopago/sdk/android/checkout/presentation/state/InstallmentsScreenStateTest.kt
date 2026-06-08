package com.mercadopago.sdk.android.checkout.presentation.state

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class InstallmentsScreenStateTest {
    @Test
    fun `given defaults then InstallmentsScreenState has empty values`() {
        val state = InstallmentsScreenState()

        assertNull(state.title)
        assertEquals(emptyList(), state.installmentsState)
        assertNull(state.footerState)
    }

    @Test
    fun `given explicit values then InstallmentsScreenState properties are assigned`() {
        val installment = InstallmentState(
            text = "3x",
            description = "of R$ 33,33",
            trailing = "R$ 100,00",
            interestFree = true,
            isSelected = true,
            number = 3,
        )
        val footer = FooterState(
            title = "Total",
            currencySymbol = "R$",
            amountIntegerPart = "100",
            amountDecimalPart = "00",
            subtitle = "subtitle",
        )

        val state = InstallmentsScreenState(
            title = "Installments",
            installmentsState = listOf(installment),
            footerState = footer,
        )

        assertEquals("Installments", state.title)
        assertEquals(listOf(installment), state.installmentsState)
        assertEquals(footer, state.footerState)
    }

    @Test
    fun `given copy with title then only title changes`() {
        val state = InstallmentsScreenState()

        val copy = state.copy(title = "Choose installments")

        assertEquals("Choose installments", copy.title)
        assertEquals(state.installmentsState, copy.installmentsState)
        assertEquals(state.footerState, copy.footerState)
        assertNotEquals(state, copy)
    }

    @Test
    fun `given InstallmentState with values then properties are assigned`() {
        val installment = InstallmentState(
            text = "6x",
            description = "of R$ 20,00",
            trailing = "R$ 120,00",
            interestFree = false,
            isSelected = false,
            number = 6,
        )

        assertEquals("6x", installment.text)
        assertEquals("of R$ 20,00", installment.description)
        assertEquals("R$ 120,00", installment.trailing)
        assertFalse(installment.interestFree)
        assertFalse(installment.isSelected)
        assertEquals(6, installment.number)
    }

    @Test
    fun `given copy on InstallmentState then isSelected changes`() {
        val installment = InstallmentState(
            text = "1x",
            description = "no interest",
            trailing = "R$ 100,00",
            interestFree = true,
            isSelected = false,
            number = 1,
        )

        val copy = installment.copy(isSelected = true)

        assertTrue(copy.isSelected)
        assertEquals(installment.text, copy.text)
        assertEquals(installment.number, copy.number)
        assertNotEquals(installment, copy)
    }
}
