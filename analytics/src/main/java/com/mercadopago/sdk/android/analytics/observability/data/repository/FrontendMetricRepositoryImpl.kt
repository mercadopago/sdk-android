package com.mercadopago.sdk.android.analytics.observability.data.repository

import com.mercadopago.sdk.android.analytics.observability.data.datasource.remote.NativeErrorRemoteDataSource
import com.mercadopago.sdk.android.analytics.observability.domain.models.PendingNativeError
import com.mercadopago.sdk.android.analytics.observability.domain.repository.NativeErrorRepository

internal class FrontendMetricRepositoryImpl(
    private val dataSource: NativeErrorRemoteDataSource,
) : NativeErrorRepository {
    override suspend fun report(error: PendingNativeError): Boolean = dataSource.report(error)
}
