package com.mercadopago.sdk.android.coremethods.builder.data.remote.mappers

import com.mercadopago.sdk.android.coremethods.builder.data.remote.request.MPDeviceSessionIdRequest
import com.mercadopago.sdk.android.coremethods.builder.domain.model.params.GetDeviceSessionParams

internal fun GetDeviceSessionParams.toRequest(): MPDeviceSessionIdRequest =
    MPDeviceSessionIdRequest(device = this.device?.fingerprint, siteId = this.siteId)
