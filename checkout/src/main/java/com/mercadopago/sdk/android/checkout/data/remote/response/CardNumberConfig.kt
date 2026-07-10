package com.mercadopago.sdk.android.checkout.data.remote.response

import com.google.gson.annotations.SerializedName

internal data class CardNumberConfig(
    @SerializedName("type")
    val type: String,
    @SerializedName("length")
    val length: LengthConfig,
    @SerializedName("mask")
    val mask: String,
)
