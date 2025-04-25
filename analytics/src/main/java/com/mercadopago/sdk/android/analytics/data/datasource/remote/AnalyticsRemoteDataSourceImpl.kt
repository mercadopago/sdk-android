package com.mercadopago.sdk.android.analytics.data.datasource.remote

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.mercadopago.sdk.android.analytics.data.remote.mapper.toRequest
import com.mercadopago.sdk.android.analytics.data.remote.models.request.AnalyticsRequest
import com.mercadopago.sdk.android.analytics.data.remote.service.AnalyticsService
import com.mercadopago.sdk.android.analytics.domain.interactor.MP_ANALYTICS_TAG
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.core.BuildConfig
import com.mercadopago.sdk.android.core.utils.KoverIgnore
import com.mercadopago.sdk.android.core.utils.isDebugApp
import com.mercadopago.sdk.android.core.utils.isSameLibraryGroup
import com.mercadopago.sdk.android.core.utils.mapToFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class AnalyticsRemoteDataSourceImpl(
    private val service: AnalyticsService,
    private val context: Context,
    private val gson: Gson,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : AnalyticsRemoteDataSource {

    override fun trackMetric(
        metric: Metric,
        siteId: String?,
        sessionId: String,
        uid: String,
    ): Flow<Unit> {
        return flow {
            val metricRequest = metric.toRequest(
                context = context,
                siteId = siteId,
                sessionId = sessionId,
                uid = uid,
            )
            if (shouldValidateTrack()) {
                validateTrack(metricRequest).firstOrNull()
            }
            emit(service.trackMetric(metricRequest))
        }
            .flowOn(coroutineDispatcher)
            .mapToFlow().map { }
    }

    @KoverIgnore("Only for test usage")
    private fun validateTrack(metricRequest: AnalyticsRequest): Flow<Unit> = flow<Unit> {
        val track = metricRequest.tracks.firstOrNull()
        val response = service.validateMetric(track)
        if (response.isSuccessful) {
            val passed = response.body()?.passed == true
            if (passed) {
                Log.d(MP_ANALYTICS_TAG, "Valid track for path ${track?.path}: ${gson.toJson(metricRequest)}")
            } else {
                Log.e(MP_ANALYTICS_TAG, "Invalid track for path ${track?.path}: ${response.errorBody()?.string()}")
            }
        } else {
            Log.e(MP_ANALYTICS_TAG, "Invalid track for path ${track?.path}: ${response.errorBody()?.string()}")
        }
    }
        .flowOn(coroutineDispatcher)
        .catch {
            Log.e(MP_ANALYTICS_TAG, "Error tracking", it)
        }

    private fun shouldValidateTrack(): Boolean = BuildConfig.DEBUG && isSameLibraryGroup(context)
}
