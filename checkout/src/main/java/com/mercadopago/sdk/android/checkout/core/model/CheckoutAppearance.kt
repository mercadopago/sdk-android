package com.mercadopago.sdk.android.checkout.core.model

import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemeAppearance
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemeProviderScheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

/**
 * CheckoutAppearance class, used to configure the checkout appearance
 * @param theme MercadoPagoThemeProviderScheme
 * @param appearance MercadoPagoThemeAppearance
 */
data class CheckoutAppearance(
    val theme: MercadoPagoThemeProviderScheme = MercadoPagoThemes.Andes,
    val appearance: MercadoPagoThemeAppearance = MercadoPagoThemeAppearance.System,
)
