package com.mercadopago.sdk.android.analytics.observability.data.remote.service

import com.mercadopago.sdk.android.analytics.observability.data.remote.models.request.NativeErrorRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal interface FrontendMetricService {
    @POST("/op-frontend-metrics/v2/error-metric")
    suspend fun report(@Body request: NativeErrorRequest): Response<Unit>
}
