package com.mercadopago.sdk.android.checkout.data.remote.response

import com.google.gson.annotations.SerializedName

internal data class LengthConfig(
    @SerializedName("min")
    val min: Int,
    @SerializedName("max")
    val max: Int,
)
