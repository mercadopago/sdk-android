package com.mercadopago.sdk.android.checkout.data.remote.service

import com.mercadopago.sdk.android.checkout.data.remote.response.PaymentBrickCardResponse
import com.mercadopago.sdk.android.checkout.data.remote.utils.PRODUCT_ID
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

private const val BRICKS_API = "cho-off"
private const val VERSION = "v1"

internal interface PaymentBrickCardService {
    @GET("$BRICKS_API/$VERSION/payment_brick/card")
    suspend fun fetch(
        @Query("order_id") orderId: String,
        @Query("bin") bin: String,
        @Query("product_id") productId: String? = PRODUCT_ID,
    ): Response<PaymentBrickCardResponse>
}
