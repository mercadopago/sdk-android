@file:Suppress("MaximumLineLength")

package com.mercadopago.sdk.android.checkout.data.preferences

import androidx.compose.ui.graphics.Color
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemeProvider
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoUserInterfaceStyle
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CheckoutThemePreferencesTest {
    private val checkoutThemePreferences: CheckoutThemePreferences = CheckoutThemePreferencesImpl()

    @Test
    fun `when class is created Then getCurrentThemeScheme returns default theme`() {
        // Given
        val expectedTheme = MercadoPagoThemes.Default

        // When
        val actualTheme = checkoutThemePreferences.getCurrentThemeScheme()

        // Then
        assertEquals(expectedTheme, actualTheme)
    }

    @Test
    fun `when class is created Then getCurrentStyle returns System style`() {
        // Given
        val expectedStyle = MercadoPagoUserInterfaceStyle.System

        // When
        val actualStyle = checkoutThemePreferences.getCurrentStyle()

        // Then
        assertEquals(expectedStyle, actualStyle)
    }

    @Test
    fun `given a new theme when setCurrentThemeScheme is called Then getCurrentThemeScheme returns that theme`() {
        // Given
        val defaultLightTheme = MercadoPagoThemes.Default.lightTheme as MercadoPagoThemeProvider.Default
        val newTheme = MercadoPagoThemes.Default.copy(
            lightTheme = defaultLightTheme.copy(
                color = defaultLightTheme.color.copy(
                    fill = defaultLightTheme.color.fill.copy(
                        accentLoud = Color.Red,
                    ),
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
    fun `given a new style when setCurrentStyle is called Then getCurrentStyle returns that style`() {
        // Given
        val newStyle = MercadoPagoUserInterfaceStyle.Light

        // When
        checkoutThemePreferences.setCurrentStyle(newStyle)
        val actualStyle = checkoutThemePreferences.getCurrentStyle()

        // Then
        assertEquals(newStyle, actualStyle)
    }

    @Test
    fun `given multiple theme changes when setCurrentThemeScheme is called sequentially Then getCurrentThemeScheme returns the last set theme`() {
        // Given
        val defaultLightTheme = MercadoPagoThemes.Default.lightTheme as MercadoPagoThemeProvider.Default
        val firstTheme = MercadoPagoThemes.Default.copy(
            lightTheme = defaultLightTheme.copy(
                color = defaultLightTheme.color.copy(
                    fill = defaultLightTheme.color.fill.copy(
                        accentLoud = Color.Red,
                    ),
                ),
            ),
        )
        val secondTheme = MercadoPagoThemes.Default

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
    fun `given multiple style changes when setCurrentStyle is called sequentially Then getCurrentStyle returns the last set style`() {
        // Given
        val firstStyle = MercadoPagoUserInterfaceStyle.Light
        val secondStyle = MercadoPagoUserInterfaceStyle.Dark

        // When
        checkoutThemePreferences.setCurrentStyle(firstStyle)

        // Then
        assertEquals(firstStyle, checkoutThemePreferences.getCurrentStyle())

        // When
        checkoutThemePreferences.setCurrentStyle(secondStyle)
        val actualStyle = checkoutThemePreferences.getCurrentStyle()

        // Then
        assertEquals(secondStyle, actualStyle)
    }
}
