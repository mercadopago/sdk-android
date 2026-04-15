package com.mercadopago.sdk.android.coremethods.builder.data.remote.request

import com.mercadolibre.android.device.sdk.domain.Device

internal data class MPDeviceSessionIdRequest(
    val device: Device?,
    val siteId: String,
)
