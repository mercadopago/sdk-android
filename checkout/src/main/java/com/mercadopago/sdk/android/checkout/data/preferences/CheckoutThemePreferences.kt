package com.mercadopago.sdk.android.checkout.data.preferences

import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemeAppearance
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemeProviderScheme

internal interface CheckoutThemePreferences {
    fun getCurrentThemeScheme(): MercadoPagoThemeProviderScheme

    fun getCurrentAppearance(): MercadoPagoThemeAppearance

    fun setCurrentThemeScheme(
        theme: MercadoPagoThemeProviderScheme,
    )

    fun setCurrentAppearance(
        appearance: MercadoPagoThemeAppearance,
    )
}
