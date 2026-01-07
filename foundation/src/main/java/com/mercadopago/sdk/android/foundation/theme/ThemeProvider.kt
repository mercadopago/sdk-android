package com.mercadopago.sdk.android.foundation.theme

import com.mercadopago.sdk.android.foundation.color.MercadoPagoAndesColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoColor
import com.mercadopago.sdk.android.foundation.outline.MercadoPagoAndesBorderWidth
import com.mercadopago.sdk.android.foundation.outline.MercadoPagoOutline
import com.mercadopago.sdk.android.foundation.shape.MercadoPagoAndesRadius
import com.mercadopago.sdk.android.foundation.shape.MercadoPagoAndesShape
import com.mercadopago.sdk.android.foundation.shape.MercadoPagoRadius
import com.mercadopago.sdk.android.foundation.shape.MercadoPagoShape
import com.mercadopago.sdk.android.foundation.spacing.MercadoPagoAndesSpacing
import com.mercadopago.sdk.android.foundation.spacing.MercadoPagoSpacing
import com.mercadopago.sdk.android.foundation.typography.MercadoPagoAndesTypography
import com.mercadopago.sdk.android.foundation.typography.MercadoPagoTypography

/**
 * Represents a complete theme configuration for MercadoPago UI components.
 * This sealed class allows for two separate theme systems: Legacy and Andes.
 * Each system contains only its own design tokens, ensuring complete separation.
 */
sealed class MercadoPagoThemeProvider {
    /**
     * Represents the legacy theme configuration for MercadoPago UI components.
     * This class holds only the legacy design tokens.
     *
     * @property color The color palette configuration for the theme
     * @property spacing The spacing system configuration for the theme
     * @property shape The shape configuration for the theme
     * @property radius The border radius configuration for the theme
     * @property outline The outline configuration for the theme
     * @property typography The typography configuration for the theme
     */
    data class Legacy(
        val color: MercadoPagoColor,
        val spacing: MercadoPagoSpacing,
        val shape: MercadoPagoShape,
        val radius: MercadoPagoRadius,
        val outline: MercadoPagoOutline,
        val typography: MercadoPagoTypography,
    ) : MercadoPagoThemeProvider()

    /**
     * Represents the Andes theme configuration for MercadoPago UI components.
     * This class holds only the Andes design tokens.
     *
     * @property color The Andes color palette configuration for the theme
     * @property spacing The Andes spacing system configuration for the theme
     * @property shape The Andes shape configuration for the theme
     * @property radius The Andes border radius configuration for the theme
     * @property borderWidth The Andes border width configuration for the theme
     * @property typography The Andes typography configuration for the theme
     */
    data class Andes(
        val color: MercadoPagoAndesColor,
        val spacing: MercadoPagoAndesSpacing,
        val shape: MercadoPagoAndesShape,
        val radius: MercadoPagoAndesRadius,
        val borderWidth: MercadoPagoAndesBorderWidth,
        val typography: MercadoPagoAndesTypography,
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
