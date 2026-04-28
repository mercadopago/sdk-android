package com.mercadopago.sdk.android.mpextended.data.datasource.remote.mapper

import com.mercadopago.sdk.android.mpextended.data.remote.response.DeviceSessionIdResponse
import com.mercadopago.sdk.android.mpextended.domain.model.MPDeviceSession

internal fun DeviceSessionIdResponse.toModel() =
    MPDeviceSession(
        session = this.sessionId,
    )
