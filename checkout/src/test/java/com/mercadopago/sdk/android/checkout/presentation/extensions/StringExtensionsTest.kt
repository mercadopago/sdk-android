package com.mercadopago.sdk.android.checkout.presentation.extensions

import com.mercadopago.sdk.android.checkout.presentation.state.AmountParts
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
internal class StringExtensionsTest {
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

    @Test
    fun `toAmountParts splits BRL label with comma decimal`() {
        val parts = "R$ 1.096,40".toAmountParts(currencySymbol = "R$")

        assertEquals("R$", parts.currencySymbol)
        assertEquals("1.096", parts.integerPart)
        assertEquals("40", parts.decimalPart)
    }

    @Test
    fun `toAmountParts splits US label with dot decimal`() {
        val parts = "US$ 1,096.40".toAmountParts(currencySymbol = "US$")

        assertEquals("US$", parts.currencySymbol)
        assertEquals("1,096", parts.integerPart)
        assertEquals("40", parts.decimalPart)
    }

    @Test
    fun `toAmountParts without decimal separator returns empty decimalPart`() {
        val parts = "¥ 1000".toAmountParts(currencySymbol = "¥")

        assertEquals("¥", parts.currencySymbol)
        assertEquals("1000", parts.integerPart)
        assertEquals("", parts.decimalPart)
    }

    @Test
    fun `toAmountParts handles missing space between symbol and digits`() {
        val parts = "R$1.000,00".toAmountParts(currencySymbol = "R$")

        assertEquals("R$", parts.currencySymbol)
        assertEquals("1.000", parts.integerPart)
        assertEquals("00", parts.decimalPart)
    }

    @Test
    fun `toAmountParts does not treat thousands dot separator as decimal`() {
        val parts = "R$ 1.000".toAmountParts(currencySymbol = "R$")

        assertEquals("R$", parts.currencySymbol)
        assertEquals("1.000", parts.integerPart)
        assertEquals("", parts.decimalPart)
    }

    @Test
    fun `toAmountParts does not treat thousands dot separator as decimal for larger values`() {
        val parts = "$ 1.096".toAmountParts(currencySymbol = "$")

        assertEquals("$", parts.currencySymbol)
        assertEquals("1.096", parts.integerPart)
        assertEquals("", parts.decimalPart)
    }

    @Test
    fun `toAmountParts with comma thousands separator and no decimal returns correct parts`() {
        val parts = "US$ 1,000".toAmountParts(currencySymbol = "US$")

        assertEquals("US$", parts.currencySymbol)
        assertEquals("1,000", parts.integerPart)
        assertEquals("", parts.decimalPart)
    }

    @Test
    fun `toAmountParts with 1-digit suffix treats it as integer part`() {
        val parts = "R$ 10,5".toAmountParts(currencySymbol = "R$")

        assertEquals("R$", parts.currencySymbol)
        assertEquals("10,5", parts.integerPart)
        assertEquals("", parts.decimalPart)
    }

    @Test
    fun `toAmountParts with 3-digit suffix treats it as integer part`() {
        val parts = "R$ 1.000,500".toAmountParts(currencySymbol = "R$")

        assertEquals("R$", parts.currencySymbol)
        assertEquals("1.000,500", parts.integerPart)
        assertEquals("", parts.decimalPart)
    }

    @Test
    fun `parseFormattedAmount splits BRL formatted string`() {
        val result = "R$ 1.096,40".parseFormattedAmount()

        assertEquals(AmountParts(currencySymbol = "R$", integerPart = "1.096", decimalPart = "40"), result)
    }

    @Test
    fun `parseFormattedAmount splits USD formatted string with dot decimal`() {
        val result = "US$ 1,096.40".parseFormattedAmount()

        assertEquals(AmountParts(currencySymbol = "US$", integerPart = "1,096", decimalPart = "40"), result)
    }

    @Test
    fun `parseFormattedAmount returns empty AmountParts when string has no digits`() {
        val result = "R$".parseFormattedAmount()

        assertEquals(AmountParts(currencySymbol = "", integerPart = "", decimalPart = ""), result)
    }

    @Test
    fun `parseFormattedAmount returns empty decimal when no decimal separator`() {
        val result = "$ 1000".parseFormattedAmount()

        assertEquals(AmountParts(currencySymbol = "$", integerPart = "1000", decimalPart = ""), result)
    }

    @Test
    fun `parseFormattedAmount handles symbol without space before digits`() {
        val result = "R\$1.000,00".parseFormattedAmount()

        assertEquals(AmountParts(currencySymbol = "R\$", integerPart = "1.000", decimalPart = "00"), result)
    }
}
