package com.mercadopago.sdk.android.threeds.domain.repository

import com.mercadopago.sdk.android.threeds.data.model.ThreeDSBody
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationResponse
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for 3DS authentication operations.
 */
internal interface ThreeDSRepository {

    /**
     * Authenticates a card token using 3DS authentication.
     *
     * @param body The request body containing card token and 3DS parameters
     * @return Flow emitting the authentication response
     */
    fun authenticate(body: ThreeDSBody): Flow<MPThreeDSAuthenticationResponse>
}
