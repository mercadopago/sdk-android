package com.mercadopago.sdk.android.coremethods.domain.model.params

/**
 * Parameters for authenticating a 3DS challenge.
 *
 * @property challengeId Unique identifier of the 3DS challenge to authenticate
 */
internal data class AuthenticateThreeDSChallengeParams(
    val challengeId: String,
)
