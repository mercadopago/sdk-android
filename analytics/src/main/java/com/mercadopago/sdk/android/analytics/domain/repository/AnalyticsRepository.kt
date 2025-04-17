package com.mercadopago.sdk.android.analytics.domain.repository

import com.mercadopago.sdk.android.analytics.domain.models.Metric
import kotlinx.coroutines.flow.Flow

internal interface AnalyticsRepository {

    suspend fun trackMetric(metric: Metric): Flow<Unit>
}
