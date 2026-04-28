package com.mercadopago.sdk.android.mpextended.data.datasource.remote

import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.mpextended.data.remote.request.MPDeviceSessionIdRequest
import com.mercadopago.sdk.android.mpextended.domain.model.MPDeviceSession

internal interface MPExtendedRemoteDataSource {
    suspend fun getDeviceSessionId(
        request: MPDeviceSessionIdRequest,
    ): Result<MPDeviceSession, ResultError>
}
