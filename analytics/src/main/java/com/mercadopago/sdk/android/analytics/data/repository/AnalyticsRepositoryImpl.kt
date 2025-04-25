package com.mercadopago.sdk.android.analytics.data.repository

import com.mercadopago.sdk.android.analytics.data.datasource.local.AnalyticsLocalDataSource
import com.mercadopago.sdk.android.analytics.data.datasource.remote.AnalyticsRemoteDataSource
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.repository.AnalyticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull

internal class AnalyticsRepositoryImpl(
    private val remoteDataSource: AnalyticsRemoteDataSource,
    private val localDataSource: AnalyticsLocalDataSource,
    private val getSiteIdFlow: Flow<String>,
) : AnalyticsRepository {

    override fun trackMetric(metric: Metric): Flow<Unit> {
        return combine(
            getSiteIdFlow,
            localDataSource.getSessionId(),
            localDataSource.getUid(),
        ) { siteId, sessionId, uid ->
            remoteDataSource.trackMetric(
                metric = metric,
                siteId = siteId,
                sessionId = sessionId.sessionId,
                uid = uid,
            ).firstOrNull()
        }
    }
}
