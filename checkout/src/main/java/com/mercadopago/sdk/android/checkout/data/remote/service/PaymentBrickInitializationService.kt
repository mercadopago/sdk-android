package com.mercadopago.sdk.android.checkout.data.remote.service

import com.mercadopago.sdk.android.checkout.data.remote.response.PaymentBrickInitializationResponse
import com.mercadopago.sdk.android.checkout.data.remote.utils.PRODUCT_ID
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

private const val BRICKS_API = "cho-off"
private const val VERSION = "v1"

internal interface PaymentBrickInitializationService {
    @GET("$BRICKS_API/$VERSION/payment_brick/initialization")
    suspend fun fetch(
        @Query("order_id") orderId: String,
        @Query("total_amount") totalAmount: String,
        @Query("customer_id") customerId: String?,
        @Query("card_ids") cardIds: String?,
        @Query("product_id") productId: String? = PRODUCT_ID,
    ): Response<PaymentBrickInitializationResponse>
}
