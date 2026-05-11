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
        val expectedTheme = MercadoPagoThemes.Default

        val actualTheme = checkoutThemePreferences.getCurrentThemeScheme()

        assertEquals(expectedTheme, actualTheme)
    }

    @Test
    fun `when class is created Then getCurrentStyle returns System style`() {
        val expectedStyle = MercadoPagoUserInterfaceStyle.System

        val actualStyle = checkoutThemePreferences.getCurrentStyle()

        assertEquals(expectedStyle, actualStyle)
    }

    @Test
    fun `given a new theme when setCurrentThemeScheme is called Then getCurrentThemeScheme returns that theme`() {
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

        checkoutThemePreferences.setCurrentThemeScheme(newTheme)
        val actualTheme = checkoutThemePreferences.getCurrentThemeScheme()

        assertEquals(newTheme, actualTheme)
    }

    @Test
    fun `given a new style when setCurrentStyle is called Then getCurrentStyle returns that style`() {
        val newStyle = MercadoPagoUserInterfaceStyle.Light

        checkoutThemePreferences.setCurrentStyle(newStyle)
        val actualStyle = checkoutThemePreferences.getCurrentStyle()

        assertEquals(newStyle, actualStyle)
    }

    @Test
    fun `given multiple theme changes when setCurrentThemeScheme is called sequentially Then getCurrentThemeScheme returns the last set theme`() {
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

        checkoutThemePreferences.setCurrentThemeScheme(firstTheme)

        assertEquals(firstTheme, checkoutThemePreferences.getCurrentThemeScheme())

        checkoutThemePreferences.setCurrentThemeScheme(secondTheme)
        val actualTheme = checkoutThemePreferences.getCurrentThemeScheme()

        assertEquals(secondTheme, actualTheme)
    }

    @Test
    fun `given multiple style changes when setCurrentStyle is called sequentially Then getCurrentStyle returns the last set style`() {
        val firstStyle = MercadoPagoUserInterfaceStyle.Light
        val secondStyle = MercadoPagoUserInterfaceStyle.Dark

        checkoutThemePreferences.setCurrentStyle(firstStyle)

        assertEquals(firstStyle, checkoutThemePreferences.getCurrentStyle())

        checkoutThemePreferences.setCurrentStyle(secondStyle)
        val actualStyle = checkoutThemePreferences.getCurrentStyle()

        assertEquals(secondStyle, actualStyle)
    }
}
