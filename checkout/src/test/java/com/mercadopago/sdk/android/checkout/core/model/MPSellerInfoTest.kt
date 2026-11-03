package com.mercadopago.sdk.android.checkout.core.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull

internal class MPSellerInfoTest {
    @Test
    fun `given no args then name and logoUrl default to null`() {
        // Given / When
        val info = MPSellerInfo()

        // Then
        assertNull(info.name)
        assertNull(info.logoUrl)
    }

    @Test
    fun `given only name then logoUrl is null`() {
        // Given / When
        val info = MPSellerInfo(name = "Adidas Store")

        // Then
        assertEquals("Adidas Store", info.name)
        assertNull(info.logoUrl)
    }

    @Test
    fun `given only logoUrl then name is null`() {
        // Given / When
        val info = MPSellerInfo(logoUrl = "https://cdn.example.com/logo.png")

        // Then
        assertNull(info.name)
        assertEquals("https://cdn.example.com/logo.png", info.logoUrl)
    }

    @Test
    fun `given both fields then exposes both`() {
        // Given / When
        val info = MPSellerInfo(name = "Adidas Store", logoUrl = "https://cdn.example.com/logo.png")

        // Then
        assertEquals("Adidas Store", info.name)
        assertEquals("https://cdn.example.com/logo.png", info.logoUrl)
    }

    @Test
    fun `given equal instances then they are equal with same hashCode`() {
        // Given
        val first = MPSellerInfo(name = "Store", logoUrl = "https://logo.png")
        val second = MPSellerInfo(name = "Store", logoUrl = "https://logo.png")

        // Then
        assertEquals(first, second)
        assertEquals(first.hashCode(), second.hashCode())
    }

    @Test
    fun `given different name then instances are not equal`() {
        // Given
        val first = MPSellerInfo(name = "Store A")
        val second = MPSellerInfo(name = "Store B")

        // Then
        assertNotEquals(first, second)
    }

    @Test
    fun `given different logoUrl then instances are not equal`() {
        // Given
        val first = MPSellerInfo(logoUrl = "https://logo-a.png")
        val second = MPSellerInfo(logoUrl = "https://logo-b.png")

        // Then
        assertNotEquals(first, second)
    }

    @Test
    fun `given copy with new name then only name changes`() {
        // Given
        val original = MPSellerInfo(name = "Store A", logoUrl = "https://logo.png")

        // When
        val updated = original.copy(name = "Store B")

        // Then
        assertEquals("Store B", updated.name)
        assertEquals("https://logo.png", updated.logoUrl)
        assertNotEquals(original, updated)
    }

    @Test
    fun `given toString then contains field values`() {
        // Given
        val info = MPSellerInfo(name = "Store", logoUrl = "https://logo.png")

        // When
        val str = info.toString()

        // Then
        assert(str.contains("Store"))
        assert(str.contains("https://logo.png"))
    }
}
