package com.mercadopago.sdk.android.checkout.data.remote.service

import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmRequest
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

private const val BRICKS_API = "cho-off"
private const val VERSION = "v1"

internal interface ReviewConfirmService {
    @POST("$BRICKS_API/$VERSION/payment_brick/review_confirm")
    suspend fun post(
        @Header("Authorization") clientToken: String,
        @Body body: ReviewConfirmRequest,
    ): Response<ReviewConfirmResponse>
}
