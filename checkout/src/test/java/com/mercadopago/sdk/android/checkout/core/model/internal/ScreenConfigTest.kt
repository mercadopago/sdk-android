package com.mercadopago.sdk.android.checkout.core.model.internal

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class ScreenConfigTest {
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
    fun `given two ReviewAndConfirm with same args but different lambdas then they are not equal`() {
        // Given
        val first = ScreenConfig.ReviewAndConfirm(onEmailChangeRequested = {})
        val second = ScreenConfig.ReviewAndConfirm(onEmailChangeRequested = {})

        // Then
        assertNotEquals(first, second)
    }

    @Test
    fun `given two ReviewAndConfirm with no args then they are equal`() {
        // Given
        val first = ScreenConfig.ReviewAndConfirm()
        val second = ScreenConfig.ReviewAndConfirm()

        // Then
        assertEquals(first, second)
        assertEquals(first.hashCode(), second.hashCode())
    }

    @Test
    fun `given ReviewAndConfirm when copy with new callback then equality changes`() {
        // Given
        val callback = {}
        val original = ScreenConfig.ReviewAndConfirm()

        // When
        val updated = original.copy(onEmailChangeRequested = callback)

        // Then
        assertNotEquals(original, updated)
        assertEquals(callback, updated.onEmailChangeRequested)
    }

    @Test
    fun `given ReviewAndConfirm then toString is not empty`() {
        // Given
        val config = ScreenConfig.ReviewAndConfirm()

        // When
        val str = config.toString()

        // Then
        assertNotNull(str)
        assert(str.isNotEmpty())
    }

    @Test
    fun `given StatusScreen with onExit then callback is accessible`() {
        // Given
        var called = false
        val config = ScreenConfig.StatusScreen(onExit = { called = true })

        // When
        config.onExit?.invoke()

        // Then
        assertTrue(called)
    }

    @Test
    fun `given StatusScreen without onExit then callback is null`() {
        // Given / When
        val config = ScreenConfig.StatusScreen()

        // Then
        assertNull(config.onExit)
    }

    @Test
    fun `given two StatusScreen with same args but different lambdas then they are not equal`() {
        // Given
        val first = ScreenConfig.StatusScreen(onExit = {})
        val second = ScreenConfig.StatusScreen(onExit = {})

        // Then
        assertNotEquals(first, second)
    }

    @Test
    fun `given two StatusScreen with no args then they are equal`() {
        // Given
        val first: ScreenConfig = ScreenConfig.StatusScreen()
        val second: ScreenConfig = ScreenConfig.StatusScreen()

        // Then
        assertEquals(first, second)
        assertEquals(first.hashCode(), second.hashCode())
    }

    @Test
    fun `given StatusScreen when copy with new callback then equality changes`() {
        // Given
        val callback = {}
        val original = ScreenConfig.StatusScreen()

        // When
        val updated = original.copy(onExit = callback)

        // Then
        assertNotEquals(original, updated)
        assertEquals(callback, updated.onExit)
    }

    @Test
    fun `given StatusScreen then toString is not empty`() {
        // Given
        val config: ScreenConfig = ScreenConfig.StatusScreen()

        // When
        val str = config.toString()

        // Then
        assertNotNull(str)
        assert(str.isNotEmpty())
    }
}
