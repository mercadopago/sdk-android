package com.mercadopago.sdk.android.checkout.core.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

internal class MPCardBrandTest {
    @Test
    fun `given predefined brands then expose expected names`() {
        assertEquals("visa", MPCardBrand.Visa.name)
        assertEquals("master", MPCardBrand.Mastercard.name)
        assertEquals("amex", MPCardBrand.Amex.name)
        assertEquals("elo", MPCardBrand.Elo.name)
        assertEquals("hipercard", MPCardBrand.Hipercard.name)
        assertEquals("diners", MPCardBrand.Diners.name)
        assertEquals("discover", MPCardBrand.Discover.name)
        assertEquals("jcb", MPCardBrand.Jcb.name)
        assertEquals("maestro", MPCardBrand.Maestro.name)
        assertEquals("unionpay", MPCardBrand.UnionPay.name)
        assertEquals("cabal", MPCardBrand.Cabal.name)
        assertEquals("naranja", MPCardBrand.Naranja.name)
    }

    @Test
    fun `given custom brand then exposes provided name`() {
        val custom = MPCardBrand.Custom("mybrand")

        assertEquals("mybrand", custom.name)
        assertTrue(custom is MPCardBrand)
    }

    @Test
    fun `given custom brand when copy with new name then equality changes`() {
        val custom = MPCardBrand.Custom("a")

        val updated = custom.copy(name = "b")

        assertEquals("b", updated.name)
        assertNotEquals(custom, updated)
        assertEquals(custom, MPCardBrand.Custom("a"))
    }

    @Test
    fun `given default list then contains all twelve predefined brands`() {
        val defaults = MPCardBrand.default

        assertEquals(12, defaults.size)
        assertTrue(defaults.contains(MPCardBrand.Visa))
        assertTrue(defaults.contains(MPCardBrand.Naranja))
    }

    @Test
    fun `given fromString with known name then returns matching predefined brand`() {
        assertSame(MPCardBrand.Visa, MPCardBrand.fromString("visa"))
        assertSame(MPCardBrand.Mastercard, MPCardBrand.fromString("master"))
    }

    @Test
    fun `given fromString with known name in different case then returns matching brand`() {
        assertSame(MPCardBrand.Amex, MPCardBrand.fromString("AMEX"))
        assertSame(MPCardBrand.Elo, MPCardBrand.fromString("Elo"))
    }

    @Test
    fun `given fromString with unknown name then returns custom brand`() {
        val result = MPCardBrand.fromString("unknown_brand")

        assertTrue(result is MPCardBrand.Custom)
        assertEquals("unknown_brand", result.name)
    }

    @Test
    fun `given every predefined brand then toString and equality execute data-object body`() {
        val brands: List<MPCardBrand> = listOf(
            MPCardBrand.Visa,
            MPCardBrand.Mastercard,
            MPCardBrand.Amex,
            MPCardBrand.Elo,
            MPCardBrand.Hipercard,
            MPCardBrand.Diners,
            MPCardBrand.Discover,
            MPCardBrand.Jcb,
            MPCardBrand.Maestro,
            MPCardBrand.UnionPay,
            MPCardBrand.Cabal,
            MPCardBrand.Naranja,
        )

        brands.forEach { brand ->
            assertEquals(brand, brand)
            assertTrue(brand.toString().isNotEmpty())
            assertEquals(brand.hashCode(), brand.hashCode())
        }
    }

    @Test
    fun `given each predefined brand then name equals expected constant`() {
        assertEquals("visa", MPCardBrand.Visa.name)
        assertEquals("master", MPCardBrand.Mastercard.name)
        assertEquals("amex", MPCardBrand.Amex.name)
        assertEquals("elo", MPCardBrand.Elo.name)
        assertEquals("hipercard", MPCardBrand.Hipercard.name)
        assertEquals("diners", MPCardBrand.Diners.name)
        assertEquals("discover", MPCardBrand.Discover.name)
        assertEquals("jcb", MPCardBrand.Jcb.name)
        assertEquals("maestro", MPCardBrand.Maestro.name)
        assertEquals("unionpay", MPCardBrand.UnionPay.name)
        assertEquals("cabal", MPCardBrand.Cabal.name)
        assertEquals("naranja", MPCardBrand.Naranja.name)
    }

    @Test
    fun `given custom brand then toString and hashCode execute data-class body`() {
        val custom = MPCardBrand.Custom("mybrand")

        assertEquals("mybrand", custom.name)
        assertTrue(custom.toString().contains("mybrand"))
        assertEquals(MPCardBrand.Custom("mybrand").hashCode(), custom.hashCode())
    }
}
