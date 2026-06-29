package com.mercadopago.sdk.android.checkout.analytics

import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemeConfiguration
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class MercadoPagoThemeConfigurationAnalyticsTest {
    @Test
    fun `when default theme then hasCustomTheme is false`() {
        assertFalse(MercadoPagoThemes.Default.hasCustomTheme)
    }

    @Test
    fun `when default theme then sellerCustomization is empty`() {
        assertEquals(emptyList(), MercadoPagoThemes.Default.sellerCustomization)
    }

    @Test
    fun `when theme differs from default then hasCustomTheme is true`() {
        val customTheme = MercadoPagoThemeConfiguration(
            lightTheme = mockk(),
            darkTheme = mockk(),
        )
        assertTrue(customTheme.hasCustomTheme)
    }

    @Test
    fun `when theme differs from default then sellerCustomization contains customized_token`() {
        val customTheme = MercadoPagoThemeConfiguration(
            lightTheme = mockk(),
            darkTheme = mockk(),
        )
        assertEquals(listOf("customized_token"), customTheme.sellerCustomization)
    }
}
