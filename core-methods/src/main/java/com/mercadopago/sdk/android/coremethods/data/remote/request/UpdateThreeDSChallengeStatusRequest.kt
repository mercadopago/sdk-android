package com.mercadopago.sdk.android.coremethods.data.remote.request

import com.google.gson.annotations.SerializedName

internal data class UpdateThreeDSChallengeStatusRequest(
    @SerializedName("status")
    val status: String,
    @SerializedName("error_detail")
    val errorDetail: ErrorDetailRequest? = null,
)

internal data class ErrorDetailRequest(
    @SerializedName("type")
    val type: String?,
    @SerializedName("code")
    val code: String?,
)
