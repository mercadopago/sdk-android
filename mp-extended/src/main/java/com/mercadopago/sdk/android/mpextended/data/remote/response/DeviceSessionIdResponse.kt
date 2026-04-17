package com.mercadopago.sdk.android.mpextended.data.remote.response

import com.google.gson.annotations.SerializedName

internal data class DeviceSessionIdResponse(
    @SerializedName("meli_session_id") val sessionId: String?,
)
