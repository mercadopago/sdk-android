package com.mercadopago.sdk.android.analytics.observability.data.datasource.remote

// @spec 20260825-native-coremethods-checkout-observability#DD-5

import com.mercadopago.sdk.android.analytics.observability.data.remote.mapper.NativeErrorRequestMapper
import com.mercadopago.sdk.android.analytics.observability.data.remote.service.FrontendMetricService
import com.mercadopago.sdk.android.analytics.observability.domain.models.PendingNativeError

private const val HTTP_ACCEPTED = 202

internal class FrontendMetricRemoteDataSourceImpl(
    private val service: FrontendMetricService,
    private val mapper: NativeErrorRequestMapper,
) : NativeErrorRemoteDataSource {
    override suspend fun report(error: PendingNativeError): Boolean {
        val response = service.report(mapper.map(error))
        val accepted = response.code() == HTTP_ACCEPTED
        if (!accepted) {
            response.errorBody()?.close()
        }
        return accepted
    }
}
