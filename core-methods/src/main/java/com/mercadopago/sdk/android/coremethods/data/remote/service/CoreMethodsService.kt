package com.mercadopago.sdk.android.coremethods.data.remote.service

import com.mercadopago.sdk.android.coremethods.data.remote.request.CardTokenBodyRequest
import com.mercadopago.sdk.android.coremethods.data.remote.response.CardTokenResponse
import com.mercadopago.sdk.android.coremethods.data.remote.response.ErrorResponse
import com.mercadopago.sdk.android.coremethods.data.remote.utils.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal interface CoreMethodsService {

    @POST("v1/card_tokens")
    suspend fun createToken(
        @Body cardTokenBody: CardTokenBodyRequest,
    ): Response<CardTokenResponse, ErrorResponse>
}
