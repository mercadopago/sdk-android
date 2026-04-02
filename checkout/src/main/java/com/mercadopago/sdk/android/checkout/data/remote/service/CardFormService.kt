package com.mercadopago.sdk.android.checkout.data.remote.service

import com.mercadopago.sdk.android.checkout.data.remote.response.CardFormInitResponse
import com.mercadopago.sdk.android.checkout.data.remote.utils.PRODUCT_ID
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

internal interface CardFormService {
    @GET("v1/card_payment_brick/initialization")
    suspend fun initialization(
        @Query("product_id") productId: String? = PRODUCT_ID,
        @Query("locale") locale: String,
    ): Response<CardFormInitResponse>
}
