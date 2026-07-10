package com.mercadopago.sdk.android.checkout.data.remote.response

import com.google.gson.annotations.SerializedName

internal data class HolderNameConfig(
    @SerializedName("type")
    val type: String,
    @SerializedName("length")
    val length: LengthConfig,
)
