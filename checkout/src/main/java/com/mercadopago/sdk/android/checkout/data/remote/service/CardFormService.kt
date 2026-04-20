package com.mercadopago.sdk.android.checkout.data.remote.service

import com.mercadopago.sdk.android.checkout.data.remote.response.CardBinResponse
import com.mercadopago.sdk.android.checkout.data.remote.utils.PRODUCT_ID
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

internal interface CardFormService {
    @Suppress("LongParameterList")
    @GET("v1/card_payment_brick/card")
    suspend fun getCardBin(
        @Query("product_id") productId: String? = PRODUCT_ID,
        @Query("bin") bin: String,
        @Query("amount") amount: String,
        @Query("processing_mode") processingMode: String,
        @Query("locale") locale: String,
        @Query("allow_payment_types") allowPaymentTypes: String?,
        @Query("allow_payment_methods") allowPaymentMethods: String?,
    ): Response<CardBinResponse>
}
