package com.mercadopago.sdk.android.checkout.data.remote.service

import com.mercadopago.sdk.android.checkout.data.remote.request.OrderProcessRequest
import com.mercadopago.sdk.android.checkout.data.remote.response.OrderProcessResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

private const val BRICKS_API = "cho-off"
private const val VERSION = "v1"

internal interface OrderService {
    @POST("$BRICKS_API/$VERSION/orders/{order_id}/process")
    suspend fun process(
        @Path("order_id") orderId: String,
        @Header("authorization") clientToken: String,
        @Body body: OrderProcessRequest,
    ): Response<OrderProcessResponse>
}
