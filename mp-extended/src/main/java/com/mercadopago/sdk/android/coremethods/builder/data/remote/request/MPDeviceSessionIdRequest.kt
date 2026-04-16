package com.mercadopago.sdk.android.coremethods.builder.data.remote.request

import com.google.gson.annotations.SerializedName
import com.mercadolibre.android.device.sdk.domain.Fingerprint

internal data class MPDeviceSessionIdRequest(
    @SerializedName("finger_print")
    val fingerprint: Fingerprint?,
    val siteId: String,
)
