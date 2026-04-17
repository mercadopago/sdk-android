package com.mercadopago.sdk.android.mpextended.data.repository

import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.mpextended.data.datasource.remote.MPExtendedRemoteDataSource
import com.mercadopago.sdk.android.mpextended.data.remote.mappers.toRequest
import com.mercadopago.sdk.android.mpextended.domain.model.MPDeviceSession
import com.mercadopago.sdk.android.mpextended.domain.model.params.GetDeviceSessionParams
import com.mercadopago.sdk.android.mpextended.domain.repository.MPExtendedRepository

internal class MpExtendedRepositoryImpl(
    private val dataSource: MPExtendedRemoteDataSource,
) : MPExtendedRepository {
    override suspend fun getDeviceSession(
        params: GetDeviceSessionParams,
    ): Result<MPDeviceSession, ResultError> {
        return dataSource.getDeviceSessionId(params.toRequest())
    }
}
