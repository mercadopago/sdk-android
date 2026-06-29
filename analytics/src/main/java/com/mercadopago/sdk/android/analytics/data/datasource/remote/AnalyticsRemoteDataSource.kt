package com.mercadopago.sdk.android.analytics.data.datasource.remote

import com.mercadopago.sdk.android.analytics.domain.models.Metric
import kotlinx.coroutines.flow.Flow

internal interface AnalyticsRemoteDataSource {

    fun trackMetric(
        metric: Metric,
        siteId: String?,
        sessionId: String,
        uid: String,
    ): Flow<Unit>
}
