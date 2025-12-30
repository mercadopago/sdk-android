package com.mercadopago.sdk.android.foundation.theme

import com.mercadopago.sdk.android.foundation.theme.andes.MercadoPagoAndesDefaultLightTheme
import com.mercadopago.sdk.android.foundation.theme.default.MercadoPagoDefaultLightTheme

/**
 * Default theme configuration for MercadoPago UI components.
 * This object provides the standard theme scheme that includes both light and dark theme configurations.
 */
object MercadoPagoDefaultThemes {
    /**
     * The default theme scheme that defines the visual appearance for both light and dark modes.
     * This is the standard theme used throughout the MercadoPago SDK.
     * Uses the legacy color system (MercadoPagoColor) as primary.
     */
    val Default = MercadoPagoThemeProviderScheme(
        lightTheme = MercadoPagoDefaultLightTheme,
        darkTheme = MercadoPagoDefaultLightTheme,
    )

    /**
     * The Andes theme scheme that defines the visual appearance for both light and dark modes.
     * This theme uses the new Andes color system (MercadoPagoAndesColor) as primary.
     */
    val Andes = MercadoPagoThemeProviderScheme(
        lightTheme = MercadoPagoAndesDefaultLightTheme,
        darkTheme = MercadoPagoAndesDefaultLightTheme,
    )
}
