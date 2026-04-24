package com.mercadopago.sdk.android.checkout.domain.extensions

import kotlin.test.Test
import kotlin.test.assertEquals

internal class MaskExtensionsTest {
    @Test
    fun `given length 8 then returns correct mask`() {
        assertEquals("#### ####", 8.toMask())
    }

    @Test
    fun `given length 9 then returns correct mask`() {
        assertEquals("#### #####", 9.toMask())
    }

    @Test
    fun `given length 10 then returns correct mask`() {
        assertEquals("#### ######", 10.toMask())
    }

    @Test
    fun `given length 11 then returns correct mask`() {
        assertEquals("#### #### ###", 11.toMask())
    }

    @Test
    fun `given length 12 then returns correct mask`() {
        assertEquals("#### #### ####", 12.toMask())
    }

    @Test
    fun `given length 13 then returns correct mask`() {
        assertEquals("#### ###### ###", 13.toMask())
    }

    @Test
    fun `given length 14 then returns correct mask`() {
        assertEquals("#### ###### ####", 14.toMask())
    }

    @Test
    fun `given length 15 then returns correct mask`() {
        assertEquals("#### ###### #####", 15.toMask())
    }

    @Test
    fun `given length 16 then returns correct mask`() {
        assertEquals("#### #### #### ####", 16.toMask())
    }

    @Test
    fun `given length 17 then returns correct mask`() {
        assertEquals("#### #### #### #####", 17.toMask())
    }

    @Test
    fun `given length 19 then returns correct mask`() {
        assertEquals(CARD_LENGTH_19_MASK, 19.toMask())
    }

    @Test
    fun `given unmapped length then returns default 16 digit mask`() {
        assertEquals("#### #### #### ####", 18.toMask())
    }
}
