package com.mercadopago.sdk.android.checkout.data.remote.response

internal data class CardNumberConfig(
    val type: String,
    val length: LengthConfig,
    val mask: String,
)
