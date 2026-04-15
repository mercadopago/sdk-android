package com.mercadopago.sdk.android.coremethods.builder.data.repository

import com.mercadopago.sdk.android.coremethods.builder.data.datasource.remote.MPExtendedRemoteDataSource
import com.mercadopago.sdk.android.coremethods.builder.data.remote.mappers.toRequest
import com.mercadopago.sdk.android.coremethods.builder.domain.model.MPDeviceSession
import com.mercadopago.sdk.android.coremethods.builder.domain.model.params.GetDeviceSessionParams
import com.mercadopago.sdk.android.coremethods.builder.domain.repository.MPExtendedRepository
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class MpExtendedRepositoryImpl(
    private val dataSource: MPExtendedRemoteDataSource,
) : MPExtendedRepository {
    override suspend fun getDeviceSession(
        params: GetDeviceSessionParams,
    ): Result<MPDeviceSession, ResultError> {
        return dataSource.getDeviceSessionId(params.toRequest())
    }
}
