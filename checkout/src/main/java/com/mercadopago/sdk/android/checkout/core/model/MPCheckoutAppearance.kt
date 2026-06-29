package com.mercadopago.sdk.android.checkout.core.model

import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemeConfiguration
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoUserInterfaceStyle

/**
 * MPCheckoutAppearance class, used to configure the checkout appearance
 * @param theme MercadoPagoThemeConfiguration
 * @param style MercadoPagoUserInterfaceStyle
 */
data class MPCheckoutAppearance(
    val theme: MercadoPagoThemeConfiguration = MercadoPagoThemes.Default,
    val style: MercadoPagoUserInterfaceStyle = MercadoPagoUserInterfaceStyle.System,
)
