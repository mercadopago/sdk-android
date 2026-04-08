package com.mercadopago.sdk.android.checkout.data.preferences

import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemeConfiguration
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoUserInterfaceStyle

internal interface CheckoutThemePreferences {
    fun getCurrentThemeScheme(): MercadoPagoThemeConfiguration

    fun getCurrentStyle(): MercadoPagoUserInterfaceStyle

    fun setCurrentThemeScheme(
        theme: MercadoPagoThemeConfiguration,
    )

    fun setCurrentStyle(
        style: MercadoPagoUserInterfaceStyle,
    )
}
