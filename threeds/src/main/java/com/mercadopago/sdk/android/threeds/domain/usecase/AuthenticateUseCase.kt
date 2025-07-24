package com.mercadopago.sdk.android.threeds.domain.usecase

import com.mercadopago.sdk.android.threeds.data.model.ThreeDSBody
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationResponse
import com.mercadopago.sdk.android.threeds.domain.repository.ThreeDSRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for authenticating a card token with 3DS.
 * This makes the authentication call to the MercadoPago backend.
 */
internal class AuthenticateUseCase(
    private val repository: ThreeDSRepository,
) {

    /**
     * Authenticates a card token using 3DS authentication.
     *
     * @param body The request body containing card token and 3DS parameters
     * @return Flow emitting the authentication response
     */
    operator fun invoke(body: ThreeDSBody): Flow<MPThreeDSAuthenticationResponse> {
        return repository.authenticate(body)
    }
}
