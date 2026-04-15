package com.mercadopago.sdk.android.coremethods.builder.data.datasource.remote.mapper

import com.mercadopago.sdk.android.coremethods.builder.data.remote.response.DeviceSessionIdResponse
import com.mercadopago.sdk.android.coremethods.builder.domain.model.MPDeviceSession

internal fun DeviceSessionIdResponse.toModel() =
    MPDeviceSession(
        session = this.sessionId,
    )
