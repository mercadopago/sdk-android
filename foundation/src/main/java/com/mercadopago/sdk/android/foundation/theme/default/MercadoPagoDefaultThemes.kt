package com.mercadopago.sdk.android.foundation.theme.default

import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemeProviderScheme

/**
 * Default theme configuration for MercadoPago UI components.
 * This object provides the standard theme scheme that includes both light and dark theme configurations.
 */
object MercadoPagoDefaultThemes {

    /**
     * The default theme scheme that defines the visual appearance for both light and dark modes.
     * This is the standard theme used throughout the MercadoPago SDK.
     */
    val Default = MercadoPagoThemeProviderScheme(
        lightTheme = MercadoPagoDefaultLightTheme,
        darkTheme = MercadoPagoDefaultLightTheme,
    )
}
