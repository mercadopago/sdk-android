@file:Suppress("MaximumLineLength")

package com.mercadopago.sdk.android.checkout.data.preferences

import androidx.compose.ui.graphics.Color
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemeAppearance
import com.mercadopago.sdk.android.foundation.theme.default.MercadoPagoDefaultThemes
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CheckoutThemePreferencesTest {
    private val checkoutThemePreferences: CheckoutThemePreferences = CheckoutThemePreferencesImpl()

    @Test
    fun `when class is created Then getCurrentThemeScheme returns default theme`() {
        // Given
        val expectedTheme = MercadoPagoDefaultThemes.Default

        // When
        val actualTheme = checkoutThemePreferences.getCurrentThemeScheme()

        // Then
        assertEquals(expectedTheme, actualTheme)
    }

    @Test
    fun `when class is created Then getCurrentAppearance returns System appearance`() {
        // Given
        val expectedAppearance = MercadoPagoThemeAppearance.System

        // When
        val actualAppearance = checkoutThemePreferences.getCurrentAppearance()

        // Then
        assertEquals(expectedAppearance, actualAppearance)
    }

    @Test
    fun `given a new theme when setCurrentThemeScheme is called Then getCurrentThemeScheme returns that theme`() {
        // Given
        val newTheme = MercadoPagoDefaultThemes.Default.copy(
            lightTheme = MercadoPagoDefaultThemes.Default.lightTheme.copy(
                color = MercadoPagoDefaultThemes.Default.lightTheme.color.copy(
                    accent = Color.Red,
                ),
            ),
        )

        // When
        checkoutThemePreferences.setCurrentThemeScheme(newTheme)
        val actualTheme = checkoutThemePreferences.getCurrentThemeScheme()

        // Then
        assertEquals(newTheme, actualTheme)
    }

    @Test
    fun `given a new appearance when setCurrentAppearance is called Then getCurrentAppearance returns that appearance`() {
        // Given
        val newAppearance = MercadoPagoThemeAppearance.Light

        // When
        checkoutThemePreferences.setCurrentAppearance(newAppearance)
        val actualAppearance = checkoutThemePreferences.getCurrentAppearance()

        // Then
        assertEquals(newAppearance, actualAppearance)
    }

    @Test
    fun `given multiple theme changes when setCurrentThemeScheme is called sequentially Then getCurrentThemeScheme returns the last set theme`() {
        // Given
        val firstTheme = MercadoPagoDefaultThemes.Default.copy(
            lightTheme = MercadoPagoDefaultThemes.Default.lightTheme.copy(
                color = MercadoPagoDefaultThemes.Default.lightTheme.color.copy(
                    accent = Color.Red,
                ),
            ),
        )
        val secondTheme = MercadoPagoDefaultThemes.Default

        // When
        checkoutThemePreferences.setCurrentThemeScheme(firstTheme)

        // Then
        assertEquals(firstTheme, checkoutThemePreferences.getCurrentThemeScheme())

        // When
        checkoutThemePreferences.setCurrentThemeScheme(secondTheme)
        val actualTheme = checkoutThemePreferences.getCurrentThemeScheme()

        // Then
        assertEquals(secondTheme, actualTheme)
    }

    @Test
    fun `given multiple appearance changes when setCurrentAppearance is called sequentially Then getCurrentAppearance returns the last set appearance`() {
        // Given
        val firstAppearance = MercadoPagoThemeAppearance.Light
        val secondAppearance = MercadoPagoThemeAppearance.Dark

        // When
        checkoutThemePreferences.setCurrentAppearance(firstAppearance)

        // Then
        assertEquals(firstAppearance, checkoutThemePreferences.getCurrentAppearance())

        // When
        checkoutThemePreferences.setCurrentAppearance(secondAppearance)
        val actualAppearance = checkoutThemePreferences.getCurrentAppearance()

        // Then
        assertEquals(secondAppearance, actualAppearance)
    }
}
