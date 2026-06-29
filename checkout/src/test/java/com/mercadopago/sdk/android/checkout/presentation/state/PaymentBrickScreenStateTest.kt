package com.mercadopago.sdk.android.checkout.presentation.state

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class PaymentBrickScreenStateTest {
    @Test
    fun `given defaults then PaymentBrickScreenState has empty values`() {
        val state = PaymentBrickScreenState()

        assertEquals("", state.title)
        assertEquals(emptyList(), state.sections)
        assertNull(state.footerState)
        assertFalse(state.isLoading)
    }

    @Test
    fun `given explicit values then PaymentBrickScreenState properties are assigned`() {
        val option = PaymentOptionState(id = "card", title = "Credit card")
        val section = PaymentSectionState(title = "Cards", options = listOf(option))
        val footer = PaymentBrickFooterState(totalLabel = "Total", totalAmount = "$ 100")

        val state = PaymentBrickScreenState(
            title = "How do you want to pay?",
            sections = listOf(section),
            footerState = footer,
            isLoading = true,
        )

        assertEquals("How do you want to pay?", state.title)
        assertEquals(listOf(section), state.sections)
        assertEquals(footer, state.footerState)
        assertTrue(state.isLoading)
    }

    @Test
    fun `given copy with isLoading then only isLoading changes`() {
        val state = PaymentBrickScreenState(title = "Title")

        val copy = state.copy(isLoading = true)

        assertTrue(copy.isLoading)
        assertEquals(state.title, copy.title)
        assertNotEquals(state, copy)
    }

    @Test
    fun `given PaymentSectionState with defaults for option then option fields are assigned`() {
        val option = PaymentOptionState(id = "pix", title = "Pix")
        val section = PaymentSectionState(title = "Instant", options = listOf(option))

        assertEquals("Instant", section.title)
        assertEquals(listOf(option), section.options)
        assertEquals("pix", option.id)
        assertEquals("Pix", option.title)
        assertNull(option.thumbnailUrl)
        assertNull(option.description)
    }

    @Test
    fun `given PaymentOptionState with all values then properties are assigned`() {
        val option = PaymentOptionState(
            id = "credit",
            title = "Credit card",
            thumbnailUrl = "https://img/thumb.png",
            description = "Up to 12x",
        )

        assertEquals("credit", option.id)
        assertEquals("Credit card", option.title)
        assertEquals("https://img/thumb.png", option.thumbnailUrl)
        assertEquals("Up to 12x", option.description)
    }

    @Test
    fun `given copy on PaymentOptionState then description changes`() {
        val option = PaymentOptionState(id = "credit", title = "Credit card")

        val copy = option.copy(description = "New desc")

        assertEquals("New desc", copy.description)
        assertEquals(option.id, copy.id)
        assertEquals(option.title, copy.title)
    }

    @Test
    fun `given PaymentBrickFooterState with values then properties are assigned and copy works`() {
        val footer = PaymentBrickFooterState(totalLabel = "Total", totalAmount = "$ 250,50")

        assertEquals("Total", footer.totalLabel)
        assertEquals("$ 250,50", footer.totalAmount)

        val copy = footer.copy(totalAmount = "$ 300,00")
        assertEquals("$ 300,00", copy.totalAmount)
        assertEquals(footer.totalLabel, copy.totalLabel)
        assertNotEquals(footer, copy)
    }
}
