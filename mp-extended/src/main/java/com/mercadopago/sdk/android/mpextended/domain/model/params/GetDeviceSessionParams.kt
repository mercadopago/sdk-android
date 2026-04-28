package com.mercadopago.sdk.android.mpextended.domain.model.params

import com.mercadolibre.android.device.sdk.domain.Device

internal data class GetDeviceSessionParams(
    val device: Device?,
    val siteId: String,
)
