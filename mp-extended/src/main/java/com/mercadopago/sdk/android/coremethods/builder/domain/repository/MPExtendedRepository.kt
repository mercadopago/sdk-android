package com.mercadopago.sdk.android.coremethods.builder.domain.repository

import com.mercadopago.sdk.android.coremethods.builder.domain.model.MPDeviceSession
import com.mercadopago.sdk.android.coremethods.builder.domain.model.params.GetDeviceSessionParams
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal interface MPExtendedRepository {
    suspend fun getDeviceSession(
        params: GetDeviceSessionParams,
    ): Result<MPDeviceSession, ResultError>
}
