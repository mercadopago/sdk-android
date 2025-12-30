package com.mercadopago.sdk.android.foundation.theme

import com.mercadopago.sdk.android.foundation.color.MercadoPagoColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoAndesColor
import com.mercadopago.sdk.android.foundation.outline.MercadoPagoOutline
import com.mercadopago.sdk.android.foundation.outline.MercadoPagoAndesBorderWidth
import com.mercadopago.sdk.android.foundation.shape.MercadoPagoRadius
import com.mercadopago.sdk.android.foundation.shape.MercadoPagoShape
import com.mercadopago.sdk.android.foundation.shape.MercadoPagoAndesRadius
import com.mercadopago.sdk.android.foundation.shape.MercadoPagoAndesShape
import com.mercadopago.sdk.android.foundation.spacing.MercadoPagoSpacing
import com.mercadopago.sdk.android.foundation.spacing.MercadoPagoAndesSpacing
import com.mercadopago.sdk.android.foundation.typography.MercadoPagoTypography
import com.mercadopago.sdk.android.foundation.typography.MercadoPagoAndesTypography

/**
 * Represents a complete theme configuration for MercadoPago UI components.
 * This class holds all the necessary design tokens that define the visual appearance
 * of components in a specific theme.
 *
 * @property color The color palette configuration for the theme (legacy)
 * @property spacing The spacing system configuration for the theme (legacy)
 * @property shape The shape configuration for the theme (legacy)
 * @property radius The border radius configuration for the theme (legacy)
 * @property outline The outline configuration for the theme (legacy)
 * @property typography The typography configuration for the theme (legacy)
 * @property newColor The new color palette configuration for the theme
 * @property newSpacing The new spacing system configuration for the theme
 * @property newShape The new shape configuration for the theme
 * @property newRadius The new border radius configuration for the theme
 * @property mercadoPagoAndesBorderWidth The new border width configuration for the theme
 * @property newTypography The new typography configuration for the theme
 */
data class MercadoPagoThemeProvider(
    val color: MercadoPagoColor,
    val spacing: MercadoPagoSpacing,
    val shape: MercadoPagoShape,
    val radius: MercadoPagoRadius,
    val outline: MercadoPagoOutline,
    val typography: MercadoPagoTypography,
    val newColor: MercadoPagoAndesColor,
    val newSpacing: MercadoPagoAndesSpacing,
    val newShape: MercadoPagoAndesShape,
    val newRadius: MercadoPagoAndesRadius,
    val mercadoPagoAndesBorderWidth: MercadoPagoAndesBorderWidth,
    val newTypography: MercadoPagoAndesTypography,
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
