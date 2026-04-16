package com.mercadopago.sdk.android.coremethods.builder.data.remote.request

import com.mercadolibre.android.device.sdk.domain.Fingerprint

internal data class MPDeviceSessionIdRequest(
    val device: Fingerprint?,
    val siteId: String,
)
