package com.mercadopago.sdk.android.checkout.data.remote.response

import com.google.gson.annotations.SerializedName

internal data class IdentificationType(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("min_length")
    val minLength: Int,
    @SerializedName("max_length")
    val maxLength: Int,
    @SerializedName("placeholder")
    val placeholder: String?,
    @SerializedName("mask")
    val mask: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("sequence")
    val sequence: String?,
)
