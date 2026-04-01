package com.mercadopago.sdk.android.foundation.theme

import com.mercadopago.sdk.android.foundation.color.MercadoPagoColor
import com.mercadopago.sdk.android.foundation.outline.MercadoPagoBorderWidth
import com.mercadopago.sdk.android.foundation.shape.MercadoPagoRadius
import com.mercadopago.sdk.android.foundation.shape.MercadoPagoShape
import com.mercadopago.sdk.android.foundation.spacing.MercadoPagoSpacing
import com.mercadopago.sdk.android.foundation.typography.MercadoPagoTypography

/**
 * Represents a complete theme configuration for MercadoPago UI components.
 */
sealed class MercadoPagoThemeProvider {
    /**
     * Represents the Andes theme configuration for MercadoPago UI components.
     *
     * @property color The color palette configuration for the theme
     * @property spacing The spacing system configuration for the theme
     * @property shape The shape configuration for the theme
     * @property radius The border radius configuration for the theme
     * @property borderWidth The border width configuration for the theme
     * @property typography The typography configuration for the theme
     */
    data class Default(
        val color: MercadoPagoColor,
        val spacing: MercadoPagoSpacing,
        val shape: MercadoPagoShape,
        val radius: MercadoPagoRadius,
        val borderWidth: MercadoPagoBorderWidth,
        val typography: MercadoPagoTypography,
    ) : MercadoPagoThemeProvider()
}

/**
 * Represents a complete theme scheme that includes both light and dark theme configurations.
 * This class allows for dynamic theme switching between light and dark modes.
 *
 * @property lightTheme The theme configuration for light mode
 * @property darkTheme The theme configuration for dark mode
 */
data class MercadoPagoThemeProviderScheme(
    val lightTheme: MercadoPagoThemeProvider,
    val darkTheme: MercadoPagoThemeProvider,
)
