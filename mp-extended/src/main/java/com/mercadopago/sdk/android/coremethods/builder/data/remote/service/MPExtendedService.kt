package com.mercadopago.sdk.android.coremethods.builder.data.remote.service

import com.mercadopago.sdk.android.coremethods.builder.data.remote.request.MPDeviceSessionIdRequest
import com.mercadopago.sdk.android.coremethods.builder.data.remote.response.DeviceSessionIdResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT

private const val BRICKS_API = "cho-off"
private const val VERSION = "v1"

internal interface MPExtendedService {
    @PUT("$BRICKS_API/$VERSION/devices/session")
    suspend fun getDeviceSession(
        @Body request: MPDeviceSessionIdRequest,
    ): Response<DeviceSessionIdResponse>
}
