package com.mercadopago.sdk.android.checkout.presentation.extensions

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals

internal class NumberExtensionsTest {
    private val brLocale = Locale("pt", "BR")

    @Test
    fun `given float value and locale then toCurrencyString formats with locale currency`() {
        val expected = NumberFormat.getCurrencyInstance(brLocale).format(100f)

        val result = 100f.toCurrencyString(brLocale)

        assertEquals(expected, result)
    }

    @Test
    fun `given BigDecimal with decimal part then getTotal returns integer part only`() {
        val result = BigDecimal("100.50").getTotal()

        assertEquals("100", result)
    }

    @Test
    fun `given BigDecimal with no decimal part then getTotal returns the integer`() {
        val result = BigDecimal("200").getTotal()

        assertEquals("200", result)
    }

    @Test
    fun `given BigDecimal that rounds up then getTotal returns rounded integer`() {
        val result = BigDecimal("100.999").getTotal()

        assertEquals("101", result)
    }

    @Test
    fun `given BigDecimal with 50 cents then getTotalDecimalPart returns 50`() {
        val result = BigDecimal("100.50").getTotalDecimalPart()

        assertEquals("50", result)
    }

    @Test
    fun `given BigDecimal with 9 cents then getTotalDecimalPart returns padded 09`() {
        val result = BigDecimal("100.09").getTotalDecimalPart()

        assertEquals("09", result)
    }

    @Test
    fun `given BigDecimal with no cents then getTotalDecimalPart returns 00`() {
        val result = BigDecimal("100").getTotalDecimalPart()

        assertEquals("00", result)
    }

    @Test
    fun `given BigDecimal with only one decimal digit then getTotalDecimalPart pads correctly`() {
        val result = BigDecimal("100.5").getTotalDecimalPart()

        assertEquals("50", result)
    }
}
