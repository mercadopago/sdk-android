package com.mercadopago.sdk.android.analytics.data.repository

import com.mercadopago.sdk.android.analytics.data.datasource.remote.NativeErrorRemoteDataSource
import com.mercadopago.sdk.android.analytics.data.remote.mapper.NativeErrorRequestMapper
import com.mercadopago.sdk.android.analytics.domain.models.PendingNativeError
import com.mercadopago.sdk.android.analytics.domain.repository.NativeErrorRepository

internal class NativeErrorRepositoryImpl(
    private val mapper: NativeErrorRequestMapper,
    private val remoteDataSource: NativeErrorRemoteDataSource,
) : NativeErrorRepository {
    override suspend fun report(error: PendingNativeError): Boolean =
        remoteDataSource.report(mapper.map(error))
}
