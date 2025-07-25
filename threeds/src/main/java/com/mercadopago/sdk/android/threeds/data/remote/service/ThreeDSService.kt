package com.mercadopago.sdk.android.threeds.data.remote.service

import com.mercadopago.sdk.android.threeds.data.remote.request.ThreeDSAuthenticationRequest
import com.mercadopago.sdk.android.threeds.data.remote.response.MPThreeDSAuthenticationResponse
import retrofit2.http.Body
import retrofit2.http.POST

internal interface ThreeDSService {

    @POST("v1/card_tokens/threeds/authenticate")
    suspend fun authenticate(
        @Body body: ThreeDSAuthenticationRequest,
    ): MPThreeDSAuthenticationResponse
}
