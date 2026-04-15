package com.mercadopago.sdk.android.coremethods.builder.data.datasource.remote

import com.mercadopago.sdk.android.coremethods.builder.data.datasource.mappers.mapSuccess
import com.mercadopago.sdk.android.coremethods.builder.data.datasource.mappers.toInternalResponse
import com.mercadopago.sdk.android.coremethods.builder.data.datasource.remote.mapper.toModel
import com.mercadopago.sdk.android.coremethods.builder.data.remote.request.MPDeviceSessionIdRequest
import com.mercadopago.sdk.android.coremethods.builder.data.remote.service.MPExtendedService
import com.mercadopago.sdk.android.coremethods.builder.domain.model.MPDeviceSession
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

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
