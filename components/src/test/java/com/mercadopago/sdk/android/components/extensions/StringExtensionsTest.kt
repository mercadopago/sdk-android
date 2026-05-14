package com.mercadopago.sdk.android.components.extensions

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StringExtensionsTest {
    @Test
    fun `given plain integer greater than zero when isPositive then returns true`() {
        assertTrue("1000".isPositive())
    }

    @Test
    fun `given formatted amount with separators when isPositive then returns true`() {
        assertTrue("1.000,50".isPositive())
    }

    @Test
    fun `given amount with currency symbol when isPositive then returns true`() {
        assertTrue("R$ 2.500,00".isPositive())
    }

    @Test
    fun `given zero when isPositive then returns false`() {
        assertFalse("0".isPositive())
    }

    @Test
    fun `given zero with decimals when isPositive then returns false`() {
        assertFalse("0,00".isPositive())
    }

    @Test
    fun `given empty string when isPositive then returns false`() {
        assertFalse("".isPositive())
    }

    @Test
    fun `given non numeric string when isPositive then returns false`() {
        assertFalse("abc".isPositive())
    }

    @Test
    fun `given string with only separators when isPositive then returns false`() {
        assertFalse(".,".isPositive())
    }

    @Test
    fun `given negative sign in string when isPositive then ignores sign and uses digits only`() {
        assertTrue("-100".isPositive())
    }
}
