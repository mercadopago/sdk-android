package com.mercadopago.sdk.android.mpextended.data.remote.mappers

import com.mercadopago.sdk.android.mpextended.data.remote.request.MPDeviceSessionIdRequest
import com.mercadopago.sdk.android.mpextended.domain.model.params.GetDeviceSessionParams

internal fun GetDeviceSessionParams.toRequest(): MPDeviceSessionIdRequest =
    MPDeviceSessionIdRequest(fingerprint = this.device?.fingerprint, siteId = this.siteId)
