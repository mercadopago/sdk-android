package com.mercadopago.sdk.android.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class MPErrorResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String
)
