package com.mercadopago.sdk.android.mpextended.domain.repository

import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.mpextended.domain.model.MPDeviceSession
import com.mercadopago.sdk.android.mpextended.domain.model.params.GetDeviceSessionParams

internal interface MPExtendedRepository {
    suspend fun getDeviceSession(
        params: GetDeviceSessionParams,
    ): Result<MPDeviceSession, ResultError>
}
