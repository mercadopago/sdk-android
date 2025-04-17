package com.mercadopago.sdk.android.coremethods.data.remote.service

import com.mercadopago.sdk.android.coremethods.data.remote.request.CardTokenBodyRequest
import com.mercadopago.sdk.android.coremethods.data.remote.response.CardIssuerResponse
import com.mercadopago.sdk.android.coremethods.data.remote.response.CardTokenResponse
import com.mercadopago.sdk.android.coremethods.data.remote.response.IdentificationTypesResponse
import com.mercadopago.sdk.android.coremethods.data.remote.response.InstallmentsResponse
import java.math.BigDecimal
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

private const val BRICKS_API = "cho-off/beta"
private const val VERSION = "v1"

internal interface CoreMethodsService {

    @POST("/v1/card_tokens")
    suspend fun createToken(
        @Body cardTokenBody: CardTokenBodyRequest,
    ): Response<CardTokenResponse>

    @GET("$BRICKS_API/$VERSION/installments")
    suspend fun getInstallments(
        @Query("product_id") productId: String?,
        @Query("bin") bin: Int?,
        @Query("processing_mode") processingMode: String?,
        @Query("amount") amount: BigDecimal?,
    ): Response<InstallmentsResponse>

    @GET("$BRICKS_API/$VERSION/identification_types")
    suspend fun getIdentificationTypes(): Response<List<IdentificationTypesResponse>>

    @GET("$BRICKS_API/$VERSION/card_issuers")
    suspend fun getCardIssuers(
        @Query("product_id") productId: String?,
        @Query("bin") bin: Int?,
        @Query("payment_method_id") paymentMethodId: String?,
    ): Response<List<CardIssuerResponse>>
}
