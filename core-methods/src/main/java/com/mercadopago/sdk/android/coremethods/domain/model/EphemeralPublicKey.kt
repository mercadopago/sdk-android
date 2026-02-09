package com.mercadopago.sdk.android.coremethods.domain.model

import com.google.gson.annotations.SerializedName

internal data class EphemeralPublicKey(
    @SerializedName("kty")
    val keyType: String,
    @SerializedName("crv")
    val curve: String,
    @SerializedName("x")
    val x: String,
    @SerializedName("y")
    val y: String,
)
