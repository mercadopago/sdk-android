package com.mercadopago.sdk.android.threeds.domain.model

/**
 * Represents a successful 3DS authentication result.
 * This is returned when the 3DS flow completes successfully.
 *
 * @param challengeResponse The response from the MercadoPago backend
 * @param challengeCompleted Whether a challenge was completed
 */
data class MPThreeDSAuthenticated(
    val challengeResponse: MPThreeDSChallengeModel,
    val challengeCompleted: Boolean = false,
)

data class MPThreeDSChallengeModel(
    val threeDSServerTransID: String,
    val acsReferenceNumber: String,
    val dsTransID: String,
    val acsTransID: String,
    val acsSignedContent: String,
)
