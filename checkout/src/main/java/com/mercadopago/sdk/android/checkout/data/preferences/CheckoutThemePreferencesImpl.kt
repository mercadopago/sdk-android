package com.mercadopago.sdk.android.checkout.data.preferences

import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemeConfiguration
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoUserInterfaceStyle

internal class CheckoutThemePreferencesImpl : CheckoutThemePreferences {
    private var theme: MercadoPagoThemeConfiguration = MercadoPagoThemes.Default
    private var style: MercadoPagoUserInterfaceStyle = MercadoPagoUserInterfaceStyle.System

    override fun getCurrentThemeScheme(): MercadoPagoThemeConfiguration = theme

    override fun getCurrentStyle(): MercadoPagoUserInterfaceStyle = style

    override fun setCurrentThemeScheme(
        theme: MercadoPagoThemeConfiguration,
    ) {
        this.theme = theme
    }

    override fun setCurrentStyle(
        style: MercadoPagoUserInterfaceStyle,
    ) {
        this.style = style
    }
}
