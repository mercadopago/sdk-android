package com.mercadopago.sdk.android.checkout.presentation.extensions

import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class StringExtensionsTest {
    @Test
    fun `given locale with known currency then getCurrencyString returns currency symbol`() {
        val result = Locale("pt", "BR").getCurrencyString()

        assertEquals("R$", result)
    }

    @Test
    fun `given null locale then getCurrencyString uses default locale`() {
        val result = null.getCurrencyString()

        assertTrue(result.isNotEmpty())
    }

    @Test
    fun `given string with all same digits then hasAllSameDigits returns true`() {
        assertTrue("1111".hasAllSameDigits())
    }

    @Test
    fun `given string with different digits then hasAllSameDigits returns false`() {
        assertFalse("1234".hasAllSameDigits())
    }

    @Test
    fun `given string with letters only then hasAllSameDigits returns false`() {
        assertFalse("aaaa".hasAllSameDigits())
    }

    @Test
    fun `given empty string then hasAllSameDigits returns false`() {
        assertFalse("".hasAllSameDigits())
    }

    @Test
    fun `given string mixed with same digit chars then hasAllSameDigits uses only digits`() {
        assertTrue("1a1b1".hasAllSameDigits())
    }

    @Test
    fun `given string shorter than previous then isBeingCleared returns true`() {
        assertTrue("abc".isBeingCleared("abcd"))
    }

    @Test
    fun `given string same length as previous then isBeingCleared returns false`() {
        assertFalse("abcd".isBeingCleared("abcd"))
    }

    @Test
    fun `given string longer than previous then isBeingCleared returns false`() {
        assertFalse("abcde".isBeingCleared("abcd"))
    }
}
