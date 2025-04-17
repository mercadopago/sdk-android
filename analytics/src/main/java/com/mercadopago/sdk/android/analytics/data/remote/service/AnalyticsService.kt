package com.mercadopago.sdk.android.analytics.data.remote.service

import com.mercadopago.sdk.android.analytics.data.remote.models.request.AnalyticsRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal interface AnalyticsService {

    @POST("/tracks")
    suspend fun trackMetric(
        @Body analyticsRequest: AnalyticsRequest
    ): Response<Unit>
}
