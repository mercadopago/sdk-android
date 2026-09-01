package com.mercadopago.sdk.android.checkout.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

internal class MPUserCancelledContextTest {
    @Test
    fun `given payment context with screens then exposes screens list`() {
        val screens = listOf(Screen.INSTALLMENTS)
        val context = MPUserCancelledContext.Payment(screens = screens)

        assertEquals(screens, context.screens)
        assertTrue(context is MPUserCancelledContext)
    }

    @Test
    fun `given payment context with empty screens then exposes empty list`() {
        val context = MPUserCancelledContext.Payment(screens = emptyList())

        assertEquals(emptyList(), context.screens)
    }

    @Test
    fun `given payment context when copy with new screens then equality changes`() {
        val context = MPUserCancelledContext.Payment(screens = emptyList())

        val updated = context.copy(screens = listOf(Screen.INSTALLMENTS))

        assertEquals(listOf(Screen.INSTALLMENTS), updated.screens)
        assertNotEquals(context, updated)
        assertEquals(context.hashCode(), context.copy().hashCode())
        assertTrue(context.toString().contains("Payment"))
    }

    @Test
    fun `given two payment contexts with same screens then they are equal`() {
        val screens = listOf(Screen.INSTALLMENTS)
        val a = MPUserCancelledContext.Payment(screens = screens)
        val b = MPUserCancelledContext.Payment(screens = screens)

        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun `given two payment contexts with different screens then they are not equal`() {
        val a = MPUserCancelledContext.Payment(screens = emptyList())
        val b = MPUserCancelledContext.Payment(screens = listOf(Screen.INSTALLMENTS))

        assertNotEquals(a, b)
    }
}
