package com.mercadopago.sdk.android.mpextended.data.datasource.remote

import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.mpextended.data.datasource.mappers.mapSuccess
import com.mercadopago.sdk.android.mpextended.data.datasource.mappers.toInternalResponse
import com.mercadopago.sdk.android.mpextended.data.datasource.remote.mapper.toModel
import com.mercadopago.sdk.android.mpextended.data.remote.request.MPDeviceSessionIdRequest
import com.mercadopago.sdk.android.mpextended.data.remote.service.MPExtendedService
import com.mercadopago.sdk.android.mpextended.domain.model.MPDeviceSession

internal class MPExtendedRemoteDataSourceImpl(
    private val service: MPExtendedService,
) : MPExtendedRemoteDataSource {
    override suspend fun getDeviceSessionId(
        request: MPDeviceSessionIdRequest,
    ): Result<MPDeviceSession, ResultError> {
        return service.getDeviceSession(request).toInternalResponse().mapSuccess {
            this.toModel()
        }
    }
}
