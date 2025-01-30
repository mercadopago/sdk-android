package com.mercadopago.sdk.android.coremethods.data.remote.response

import com.google.gson.annotations.SerializedName

internal data class ErrorResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String
)
