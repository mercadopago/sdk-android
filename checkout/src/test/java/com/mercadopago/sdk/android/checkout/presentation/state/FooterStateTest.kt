package com.mercadopago.sdk.android.checkout.presentation.state

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class FooterStateTest {
    @Test
    fun `given explicit values then properties are assigned`() {
        val state = FooterState(
            title = "Total",
            currencySymbol = "R$",
            amountIntegerPart = "100",
            amountDecimalPart = "00",
            subtitle = "in 3 installments",
        )

        assertEquals("Total", state.title)
        assertEquals("R$", state.currencySymbol)
        assertEquals("100", state.amountIntegerPart)
        assertEquals("00", state.amountDecimalPart)
        assertEquals("in 3 installments", state.subtitle)
    }

    @Test
    fun `given copy with changed title then only title changes`() {
        val state = FooterState(
            title = "Total",
            currencySymbol = "R$",
            amountIntegerPart = "100",
            amountDecimalPart = "00",
            subtitle = "subtitle",
        )

        val copy = state.copy(title = "Amount")

        assertEquals("Amount", copy.title)
        assertEquals(state.currencySymbol, copy.currencySymbol)
        assertEquals(state.amountIntegerPart, copy.amountIntegerPart)
        assertEquals(state.amountDecimalPart, copy.amountDecimalPart)
        assertEquals(state.subtitle, copy.subtitle)
        assertNotEquals(state, copy)
    }
}
