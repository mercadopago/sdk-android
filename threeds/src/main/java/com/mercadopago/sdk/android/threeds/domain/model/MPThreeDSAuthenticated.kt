package com.mercadopago.sdk.android.threeds.domain.model

/**
 * Represents a successful 3DS authentication result.
 * This is returned when the 3DS flow completes successfully.
 *
 * @param authenticationResponse The response from the MercadoPago backend
 * @param challengeCompleted Whether a challenge was completed
 */
data class MPThreeDSAuthenticated(
    val authenticationResponse: MPThreeDSAuthenticationResponse,
    val challengeCompleted: Boolean = false,
)
