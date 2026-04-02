package com.mercadopago.sdk.android.checkout.data.remote.service

import com.mercadopago.sdk.android.checkout.data.remote.response.CardFormInitResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

internal interface CardFormService {
    @GET("v1/card_payment_brick/initialization")
    suspend fun initialization(
        @Query("public_key") publicKey: String,
        @Header("product_id") productId: String,
        @Header("locale") locale: String,
    ): Response<CardFormInitResponse>
}
