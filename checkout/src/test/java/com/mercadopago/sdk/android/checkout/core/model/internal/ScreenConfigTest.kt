package com.mercadopago.sdk.android.checkout.core.model.internal

import com.mercadopago.sdk.android.checkout.core.model.MPSellerInfo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class ScreenConfigTest {
    @Test
    fun `given ReviewAndConfirm with no args then seller is null`() {
        // Given / When
        val config = ScreenConfig.ReviewAndConfirm()

        // Then
        assertNull(config.seller)
    }

    @Test
    fun `given ReviewAndConfirm with seller then seller is exposed`() {
        // Given
        val seller = MPSellerInfo(name = "Adidas Store", logoUrl = "https://logo.png")

        // When
        val config = ScreenConfig.ReviewAndConfirm(seller = seller)

        // Then
        assertEquals(seller, config.seller)
    }

    @Test
    fun `given ReviewAndConfirm with onEmailChangeRequested then callback is accessible`() {
        // Given
        var called = false
        val config = ScreenConfig.ReviewAndConfirm(onEmailChangeRequested = { called = true })

        // When
        config.onEmailChangeRequested?.invoke()

        // Then
        assertTrue(called)
    }

    @Test
    fun `given ReviewAndConfirm without onEmailChangeRequested then callback is null`() {
        // Given / When
        val config = ScreenConfig.ReviewAndConfirm()

        // Then
        assertNull(config.onEmailChangeRequested)
    }

    @Test
    fun `given two ReviewAndConfirm with same seller but different lambdas then they are not equal`() {
        // Given
        val seller = MPSellerInfo(name = "Store")
        val first = ScreenConfig.ReviewAndConfirm(seller = seller, onEmailChangeRequested = {})
        val second = ScreenConfig.ReviewAndConfirm(seller = seller, onEmailChangeRequested = {})

        // Then
        assertNotEquals(first, second)
    }

    @Test
    fun `given two ReviewAndConfirm with same seller then they are equal`() {
        // Given
        val seller = MPSellerInfo(name = "Store")
        val first = ScreenConfig.ReviewAndConfirm(seller = seller)
        val second = ScreenConfig.ReviewAndConfirm(seller = seller)

        // Then
        assertEquals(first, second)
        assertEquals(first.hashCode(), second.hashCode())
    }

    @Test
    fun `given two ReviewAndConfirm with different sellers then they are not equal`() {
        // Given
        val first = ScreenConfig.ReviewAndConfirm(seller = MPSellerInfo(name = "Store A"))
        val second = ScreenConfig.ReviewAndConfirm(seller = MPSellerInfo(name = "Store B"))

        // Then
        assertNotEquals(first, second)
    }

    @Test
    fun `given ReviewAndConfirm when copy with new seller then equality changes`() {
        // Given
        val original = ScreenConfig.ReviewAndConfirm(seller = MPSellerInfo(name = "Store A"))

        // When
        val updated = original.copy(seller = MPSellerInfo(name = "Store B"))

        // Then
        assertNotEquals(original, updated)
        assertEquals(MPSellerInfo(name = "Store B"), updated.seller)
    }

    @Test
    fun `given ReviewAndConfirm then toString is not empty`() {
        // Given
        val config = ScreenConfig.ReviewAndConfirm(seller = MPSellerInfo(name = "Store"))

        // When
        val str = config.toString()

        // Then
        assertNotNull(str)
        assert(str.isNotEmpty())
    }
}
