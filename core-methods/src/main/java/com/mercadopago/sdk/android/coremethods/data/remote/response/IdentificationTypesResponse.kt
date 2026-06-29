package com.mercadopago.sdk.android.coremethods.data.remote.response

import com.google.gson.annotations.SerializedName

internal data class IdentificationTypesResponse(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("min_length")
    val minLength: Int? = null,
    @SerializedName("max_length")
    val maxLength: Int? = null,
)
