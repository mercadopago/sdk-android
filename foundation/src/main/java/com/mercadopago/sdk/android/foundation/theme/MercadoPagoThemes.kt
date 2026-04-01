package com.mercadopago.sdk.android.foundation.theme

import com.mercadopago.sdk.android.foundation.theme.andes.MercadoPagoAndesDefaultLightTheme

/**
 * Default theme configuration for MercadoPago UI components.
 * This object provides the standard theme scheme that includes both light and dark theme configurations.
 */
object MercadoPagoThemes {
    /**
     * The Andes theme scheme that defines the visual appearance for both light and dark modes.
     */
    val Andes = MercadoPagoThemeProviderScheme(
        lightTheme = MercadoPagoAndesDefaultLightTheme,
        darkTheme = MercadoPagoAndesDefaultLightTheme,
    )
}
