package com.mercadopago.sdk.android.threeds.data.remote.service

import com.mercadopago.sdk.android.threeds.data.model.ThreeDSBody
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit service interface for 3DS authentication API calls.
 */
internal interface ThreeDSService {

    /**
     * Authenticates a card token using 3DS authentication.
     *
     * @param body The request body containing card token and 3DS parameters
     * @return The authentication response from MercadoPago backend
     */
    @POST("v1/card_tokens/threeds/authenticate")
    suspend fun authenticate(@Body body: ThreeDSBody): MPThreeDSAuthenticationResponse
}
