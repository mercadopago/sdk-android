package com.mercadopago.sdk.android.checkout.presentation.shared

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

internal class ButtonStateTest {
    @Test
    fun `given default values then button is disabled and not loading`() {
        val state = ButtonState()

        assertFalse(state.enabled)
        assertFalse(state.isLoading)
    }

    @Test
    fun `given explicit values then properties are assigned`() {
        val state = ButtonState(enabled = true, isLoading = true)

        assertTrue(state.enabled)
        assertTrue(state.isLoading)
    }

    @Test
    fun `given copy with changed enabled then only enabled changes`() {
        val state = ButtonState(enabled = false, isLoading = true)

        val copy = state.copy(enabled = true)

        assertTrue(copy.enabled)
        assertEquals(state.isLoading, copy.isLoading)
        assertNotEquals(state, copy)
    }
}
