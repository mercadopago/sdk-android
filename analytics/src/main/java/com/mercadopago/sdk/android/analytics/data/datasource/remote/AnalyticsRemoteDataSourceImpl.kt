package com.mercadopago.sdk.android.analytics.data.datasource.remote

import android.content.Context
import com.mercadopago.sdk.android.analytics.data.remote.mapper.toRequest
import com.mercadopago.sdk.android.analytics.data.remote.service.AnalyticsService
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.core.utils.toKotlinResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class AnalyticsRemoteDataSourceImpl(
    private val service: AnalyticsService,
    private val context: Context,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : AnalyticsRemoteDataSource {

    override suspend fun trackMetric(
        metric: Metric,
        siteId: String?,
        sessionId: String,
        uid: String,
    ): Result<Unit> {
        return withContext(coroutineDispatcher) {
            service.trackMetric(
                metric.toRequest(
                    context = context,
                    siteId = siteId,
                    sessionId = sessionId,
                    uid = uid,
                )
            ).toKotlinResponse()
        }
    }
}
