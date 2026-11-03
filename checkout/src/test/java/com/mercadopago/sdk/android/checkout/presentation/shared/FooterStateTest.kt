package com.mercadopago.sdk.android.checkout.presentation.shared

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class FooterStateTest {
    @Test
    fun `given explicit values then properties are assigned`() {
        val state = FooterState(
            title = "Total",
            currencySymbol = "R$",
            amountIntegerPart = "100",
            amountDecimalPart = "00",
            subtitle = "in 3 installments",
            buttonLabel = "Pay",
            isVisible = true,
            buttonState = ButtonState(enabled = true),
        )

        assertEquals("Total", state.title)
        assertEquals("R$", state.currencySymbol)
        assertEquals("100", state.amountIntegerPart)
        assertEquals("00", state.amountDecimalPart)
        assertEquals("in 3 installments", state.subtitle)
        assertEquals("Pay", state.buttonLabel)
        assertTrue(state.isVisible)
        assertEquals(ButtonState(enabled = true), state.buttonState)
    }

    @Test
    fun `given default values then buttonState is null`() {
        assertNull(FooterState().buttonState)
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

    @Test
    fun `given null buttonState when withButtonEnabled then creates enabled ButtonState`() {
        val result = FooterState().withButtonEnabled(true)

        assertEquals(ButtonState(enabled = true, isLoading = false), result.buttonState)
    }

    @Test
    fun `given existing buttonState when withButtonEnabled then preserves isLoading`() {
        val state = FooterState(buttonState = ButtonState(enabled = false, isLoading = true))

        val result = state.withButtonEnabled(true)

        assertEquals(ButtonState(enabled = true, isLoading = true), result.buttonState)
    }

    @Test
    fun `given null buttonState when withButtonLoading then creates loading ButtonState`() {
        val result = FooterState().withButtonLoading(true)

        assertEquals(ButtonState(enabled = false, isLoading = true), result.buttonState)
    }

    @Test
    fun `given existing buttonState when withButtonLoading then preserves enabled`() {
        val state = FooterState(buttonState = ButtonState(enabled = true, isLoading = false))

        val result = state.withButtonLoading(true)

        assertEquals(ButtonState(enabled = true, isLoading = true), result.buttonState)
    }
}
