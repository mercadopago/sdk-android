package com.mercadopago.sdk.android.checkout.data.remote.response

import com.google.gson.annotations.SerializedName

internal data class SecurityCodeConfig(
    @SerializedName("type")
    val type: String,
    @SerializedName("length")
    val length: Int,
    @SerializedName("card_location")
    val cardLocation: String? = null,
    @SerializedName("tooltip")
    val tooltip: String? = null,
    @SerializedName("placeholder")
    val placeholder: String? = null,
)
