package com.mercadopago.sdk.android.checkout.data.remote.response

internal data class ExpirationDateConfig(
    val type: String,
    val mask: String,
    val length: LengthConfig,
)
