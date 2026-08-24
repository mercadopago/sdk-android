package com.mercadopago.sdk.android.checkout.data.remote.service

import com.mercadopago.sdk.android.checkout.data.remote.response.CardBinResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.CardFormInitResponse
import com.mercadopago.sdk.android.checkout.data.remote.utils.PRODUCT_ID
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

private const val BRICKS_API = "cho-off"
private const val VERSION = "v1"

internal interface CardFormService {
    @GET("$BRICKS_API/$VERSION/card_payment_brick/initialization")
    suspend fun initialization(
        @Header("Authorization") authorization: String?,
        @Query("product_id") productId: String? = PRODUCT_ID,
        @Query("order_id") orderId: String?,
        @Query("checkout_type") checkoutType: String,
        @Query("screens") screens: String? = null,
    ): Response<CardFormInitResponse>

    @Suppress("LongParameterList")
    @GET("$BRICKS_API/$VERSION/card_payment_brick/card")
    suspend fun getCardBin(
        @Query("product_id") productId: String? = PRODUCT_ID,
        @Query("bin") bin: String,
        @Query("checkout_type") checkoutType: String,
        @Query("processing_mode") processingMode: String,
        @Query("amount") amount: String,
        @Query("excluded_payment_types") excludedPaymentTypes: String?,
        @Query("excluded_payment_methods") excludedPaymentMethods: String?,
    ): Response<CardBinResponse>
}
