package com.mercadopago.sdk.android.checkout.data.preferences

import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemeAppearance
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemeProviderScheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

internal class CheckoutThemePreferencesImpl : CheckoutThemePreferences {
    private var theme: MercadoPagoThemeProviderScheme = MercadoPagoThemes.Andes
    private var appearance: MercadoPagoThemeAppearance = MercadoPagoThemeAppearance.System

    override fun getCurrentThemeScheme(): MercadoPagoThemeProviderScheme = theme

    override fun getCurrentAppearance(): MercadoPagoThemeAppearance = appearance

    override fun setCurrentThemeScheme(
        theme: MercadoPagoThemeProviderScheme,
    ) {
        this.theme = theme
    }

    override fun setCurrentAppearance(
        appearance: MercadoPagoThemeAppearance,
    ) {
        this.appearance = appearance
    }
}
