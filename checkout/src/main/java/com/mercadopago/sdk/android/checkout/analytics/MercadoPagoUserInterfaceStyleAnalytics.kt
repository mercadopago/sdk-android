package com.mercadopago.sdk.android.checkout.analytics

import com.mercadopago.sdk.android.core.utils.KoverIgnore
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoUserInterfaceStyle

@KoverIgnore("in development")
internal fun MercadoPagoUserInterfaceStyle.toAnalyticsString(): String =
    when (this) {
        MercadoPagoUserInterfaceStyle.System -> "system"
        MercadoPagoUserInterfaceStyle.Light -> "light"
        MercadoPagoUserInterfaceStyle.Dark -> "dark"
    }
