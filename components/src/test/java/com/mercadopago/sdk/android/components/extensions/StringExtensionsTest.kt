package com.mercadopago.sdk.android.components.extensions

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StringExtensionsTest {
    @Test
    fun `given plain integer greater than zero when isGreaterThan then returns true`() {
        assertTrue("1000".isGreaterThan())
    }

    @Test
    fun `given formatted amount with separators when isGreaterThan then returns true`() {
        assertTrue("1.000,50".isGreaterThan())
    }

    @Test
    fun `given amount with currency symbol when isGreaterThan then returns true`() {
        assertTrue("R$ 2.500,00".isGreaterThan())
    }

    @Test
    fun `given zero when isGreaterThan then returns false`() {
        assertFalse("0".isGreaterThan())
    }

    @Test
    fun `given zero with decimals when isGreaterThan then returns false`() {
        assertFalse("0,00".isGreaterThan())
    }

    @Test
    fun `given empty string when isGreaterThan then returns false`() {
        assertFalse("".isGreaterThan())
    }

    @Test
    fun `given non numeric string when isGreaterThan then returns false`() {
        assertFalse("abc".isGreaterThan())
    }

    @Test
    fun `given string with only separators when isGreaterThan then returns false`() {
        assertFalse(".,".isGreaterThan())
    }

    @Test
    fun `given negative sign in string when isGreaterThan then ignores sign and uses digits only`() {
        assertTrue("-100".isGreaterThan())
    }
}
