package com.mercadopago.sdk.android.checkout.presentation.extensions

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class IntExtensionsTest {
    @Test
    fun `given count of 3 then toCountStringPlaceholder produces correct string`() {
        val result = 3.toCountStringPlaceholder("CVV")

        assertEquals("CVV 123", result)
    }

    @Test
    fun `given count of 4 then toCountStringPlaceholder produces correct string`() {
        val result = 4.toCountStringPlaceholder("Code")

        assertEquals("Code 1234", result)
    }

    @Test
    fun `given count of 1 then toCountStringPlaceholder produces single digit`() {
        val result = 1.toCountStringPlaceholder("X")

        assertEquals("X 1", result)
    }

    @Test
    fun `given current length less than previous then isBeingCleared returns true`() {
        assertTrue(3.isBeingCleared(previousLength = 4))
    }

    @Test
    fun `given current length equals previous then isBeingCleared returns false`() {
        assertFalse(4.isBeingCleared(previousLength = 4))
    }

    @Test
    fun `given current length greater than previous then isBeingCleared returns false`() {
        assertFalse(5.isBeingCleared(previousLength = 4))
    }
}
