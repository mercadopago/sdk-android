package com.mercadopago.sdk.android.analytics.domain.usecase

import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.repository.AnalyticsRepository
import kotlinx.coroutines.flow.Flow

internal class TrackMetricUseCase(
    private val analyticsRepository: AnalyticsRepository,
) {

    operator fun invoke(metric: Metric): Flow<Unit> {
        return analyticsRepository.trackMetric(metric)
    }
}
