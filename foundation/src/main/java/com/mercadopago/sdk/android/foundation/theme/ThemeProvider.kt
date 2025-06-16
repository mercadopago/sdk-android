package com.mercadopago.sdk.android.foundation.theme

import com.mercadopago.sdk.android.foundation.color.MercadoPagoColor
import com.mercadopago.sdk.android.foundation.outline.MercadoPagoOutline
import com.mercadopago.sdk.android.foundation.shape.MercadoPagoRadius
import com.mercadopago.sdk.android.foundation.shape.MercadoPagoShape
import com.mercadopago.sdk.android.foundation.spacing.MercadoPagoSpacing

/**
 * Represents a complete theme configuration for MercadoPago UI components.
 * This class holds all the necessary design tokens that define the visual appearance
 * of components in a specific theme.
 *
 * @property color The color palette configuration for the theme
 * @property spacing The spacing system configuration for the theme
 * @property shape The shape configuration for the theme
 * @property radius The border radius configuration for the theme
 * @property outline The outline configuration for the theme
 */
data class MercadoPagoThemeProvider(
    val color: MercadoPagoColor,
    val spacing: MercadoPagoSpacing,
    val shape: MercadoPagoShape,
    val radius: MercadoPagoRadius,
    val outline: MercadoPagoOutline,
)

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
