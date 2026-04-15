package com.mercadopago.sdk.android.coremethods.builder.domain.model.params

import com.mercadolibre.android.device.sdk.domain.Device

internal data class GetDeviceSessionParams(
    val device: Device?,
    val siteId: String,
)
