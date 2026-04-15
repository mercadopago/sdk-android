package com.mercadopago.sdk.android.coremethods.builder.data.datasource.remote

import com.mercadopago.sdk.android.coremethods.builder.data.remote.request.MPDeviceSessionIdRequest
import com.mercadopago.sdk.android.coremethods.builder.domain.model.MPDeviceSession
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal interface MPExtendedRemoteDataSource {
    suspend fun getDeviceSessionId(
        request: MPDeviceSessionIdRequest,
    ): Result<MPDeviceSession, ResultError>
}
