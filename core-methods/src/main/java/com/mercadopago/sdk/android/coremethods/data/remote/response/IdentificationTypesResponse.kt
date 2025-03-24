package com.mercadopago.sdk.android.coremethods.data.remote.response

import com.google.gson.annotations.SerializedName

data class IdentificationTypesResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("min_length")
    val minLength: Int,
    @SerializedName("max_length")
    val maxLength: Int,
)
