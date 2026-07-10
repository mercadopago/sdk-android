package com.mercadopago.sdk.android.checkout.data.remote.response

import com.google.gson.annotations.SerializedName

internal data class ExpirationDateConfig(
    @SerializedName("type")
    val type: String,
    @SerializedName("mask")
    val mask: String,
    @SerializedName("length")
    val length: LengthConfig,
)
