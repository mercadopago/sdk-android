package com.mercadopago.sdk.android.analytics.data.remote.service

import com.mercadopago.sdk.android.analytics.data.remote.models.request.NativeErrorRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal interface NativeErrorService {
    @POST("/op-frontend-metrics/v2/error-metric")
    suspend fun report(@Body request: NativeErrorRequest): Response<Unit>
}
