package com.mercadopago.sdk.android.checkout.analytics

import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemeConfiguration
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

internal val MercadoPagoThemeConfiguration.hasCustomTheme: Boolean
    get() = this != MercadoPagoThemes.Default

internal val MercadoPagoThemeConfiguration.sellerCustomization: List<String>
    get() = if (hasCustomTheme) listOf("customized_token") else emptyList()
