package com.mercadopago.sdk.android.analytics.data.datasource.remote

import com.mercadopago.sdk.android.analytics.domain.models.Metric

internal interface AnalyticsRemoteDataSource {

    suspend fun trackMetric(
        metric: Metric,
        siteId: String?,
        sessionId: String,
        uid: String,
    ): Result<Unit>
}
