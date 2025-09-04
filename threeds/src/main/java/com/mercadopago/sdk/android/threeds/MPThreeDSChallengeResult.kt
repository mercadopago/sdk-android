package com.mercadopago.sdk.android.threeds

/**
 * Result of a 3DS challenge operation.
 */
sealed class MPThreeDSChallengeResult {
    /**
     * Challenge completed successfully.
     *
     * @param result The authentication result
     */
    data class OnSuccess(val result: MPThreeDSAuthenticated) : MPThreeDSChallengeResult()

    /**
     * Challenge failed with an error.
     *
     * @param error The error that occurred
     */
    data class OnError(val error: MPThreeDSChallengeError) : MPThreeDSChallengeResult()

    /**
     * Challenge was cancelled by the user.
     */
    data object OnCancel : MPThreeDSChallengeResult()

    /**
     * Challenge timed out.
     */
    data object OnTimedOut : MPThreeDSChallengeResult()
}

/**
 * Represents a successful 3DS authentication result.
 *
 * @param challengeResponse The challenge response data
 * @param challengeCompleted Whether a challenge was completed
 */
data class MPThreeDSAuthenticated(
    val challengeResponse: MPThreeDSChallengeResponse,
    val challengeCompleted: Boolean = false,
)

/**
 * Contains the 3DS challenge response data.
 *
 * @param threeDSServerTransID 3DS server transaction ID
 * @param acsReferenceNumber ACS reference number
 * @param dsTransID Directory server transaction ID
 * @param acsTransID ACS transaction ID
 * @param acsSignedContent ACS signed content
 */
data class MPThreeDSChallengeResponse(
    val threeDSServerTransID: String,
    val acsReferenceNumber: String,
    val dsTransID: String,
    val acsTransID: String,
    val acsSignedContent: String,
)

/**
 * Represents an error that occurred during 3DS challenge.
 *
 * @param code Error code
 * @param message Error message
 * @param details Additional error details (optional)
 * @param cause The underlying cause (optional)
 */
data class MPThreeDSChallengeError(
    val code: String,
    override val message: String,
    val details: String? = null,
    override val cause: Throwable? = null,
) : Exception(message, cause)
