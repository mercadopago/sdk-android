package com.mercadopago.sdk.android.analytics.data.remote.service

import com.mercadopago.sdk.android.analytics.data.remote.models.request.AnalyticsRequest
import com.mercadopago.sdk.android.analytics.data.remote.models.request.TrackRequest
import com.mercadopago.sdk.android.analytics.data.remote.models.response.ValidateMetricResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal interface AnalyticsService {

    @POST("/tracks")
    suspend fun trackMetric(
        @Body analyticsRequest: AnalyticsRequest
    ): Response<Unit>

    @POST("/melidata/catalog/validate")
    suspend fun validateMetric(
        @Body trackRequest: TrackRequest? = null,
    ): Response<ValidateMetricResponse?>
}
