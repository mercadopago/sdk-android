package com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PCIFieldStateTest {
    @Test
    fun `PCIFieldState initializes with empty input`() {
        val state = PCIFieldState()
        assertEquals("", state.input)
    }

    @Test
    fun `PCIFieldState input can be set and read`() {
        val state = PCIFieldState()
        state.input = "4111111111111111"
        assertEquals("4111111111111111", state.input)
    }

    @Test
    fun `isEmpty returns true when input is empty`() {
        val state = PCIFieldState()
        assertTrue(state.isEmpty)
    }

    @Test
    fun `isEmpty returns false when input has content`() {
        val state = PCIFieldState()
        state.input = "123"
        assertFalse(state.isEmpty)
    }

    @Test
    fun `PCIFieldState does not contain Saver companion`() {
        val companionMembers = PCIFieldState.Companion::class.members.map { it.name }
        assertFalse(
            companionMembers.any { it.contains("Saver", ignoreCase = true) },
            "PCIFieldState should not contain a Saver to prevent PCI data serialization to Bundle/disk",
        )
    }
}
