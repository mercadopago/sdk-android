package com.mercadopago.sdk.android.coremethods.data.remote.service

import com.mercadopago.sdk.android.coremethods.data.remote.request.CardTokenBodyRequest
import com.mercadopago.sdk.android.coremethods.data.remote.response.CardTokenResponse
import com.mercadopago.sdk.android.coremethods.data.remote.response.IdentificationTypesResponse
import com.mercadopago.sdk.android.coremethods.data.remote.response.InstallmentsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

internal interface CoreMethodsService {
    @POST("v1/card_tokens")
    suspend fun createToken(
        @Body cardTokenBody: CardTokenBodyRequest,
    ): Response<CardTokenResponse>

    @POST("v1/payment_methods/installments")
    suspend fun getInstallments(
        @Query("product_id") productId: String?,
        @Query("bin") bin: Int?,
        @Query("processing_mode") processingMode: String?,
        @Query("amount") amount: Long?,
    ): Response<InstallmentsResponse>

    @POST("v1/identification_types")
    suspend fun getIdentificationTypes(): Response<List<IdentificationTypesResponse>>
}
