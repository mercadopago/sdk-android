package com.mercadopago.sdk.android.checkout.statusscreen.data.remote.service

import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.request.StatusScreenRequest
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

private const val BRICKS_API = "cho-off"
private const val VERSION = "v1"

internal interface StatusScreenService {
    @POST("$BRICKS_API/$VERSION/payment_brick/status_screen")
    suspend fun fetch(
        @Header("Authorization") clientToken: String,
        @Body body: StatusScreenRequest,
        @Query("checkout_type") checkoutType: String? = null,
    ): Response<StatusScreenResponse>
}
