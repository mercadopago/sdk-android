package com.mercadopago.sdk.android.coremethods.builder.data.remote.response

import com.google.gson.annotations.SerializedName

internal data class DeviceSessionIdResponse(
    @SerializedName("session_id") val sessionId: String?,
)
