package com.mercadopago.sdk.android.coremethods.domain.provider.models

/**
 * Sealed class representing the result of a 3DS challenge flow.
 * This can be a successful authentication, an error, a cancellation, or a timeout.
 */
sealed class ThreeDSChallengeResult {
    /**
     * Called when the 3DS authentication completes successfully.
     *
     * @property result The authentication result containing response data
     */
    data class OnSuccess(val result: ThreeDSAuthenticated) : ThreeDSChallengeResult()

    /**
     * Called when the 3DS authentication fails or encounters an error.
     *
     * @property error The error that occurred during authentication
     */
    data class OnError(val error: ThreeDSChallengeError) : ThreeDSChallengeResult()

    /**
     * Called when the user cancels the 3DS challenge.
     */
    data object OnCancel : ThreeDSChallengeResult()

    /**
     * Called when the 3DS challenge times out.
     */
    data object OnTimedOut : ThreeDSChallengeResult()
}

/**
 * Represents a successful 3DS authentication result.
 * This is returned when the 3DS flow completes successfully.
 *
 * @property challengeResponse The response from the MercadoPago backend
 * @property challengeCompleted Whether a challenge was completed
 */
data class ThreeDSAuthenticated(
    val challengeResponse: ThreeDSChallengeModel,
    val challengeCompleted: Boolean = false,
)

/**
 * Represents the challenge authentication response.
 *
 * @property threeDSServerTransID Challenge authentication parameter
 * @property acsReferenceNumber Challenge authentication parameter
 * @property dsTransID Challenge authentication parameter
 * @property acsTransID Challenge authentication parameter
 * @property acsSignedContent Challenge authentication parameter
 */
data class ThreeDSChallengeModel(
    val threeDSServerTransID: String,
    val acsReferenceNumber: String,
    val dsTransID: String,
    val acsTransID: String,
    val acsSignedContent: String,
)

/**
 * Represents an error that occurred during the 3DS challenge process.
 *
 * @property code Error code from the 3DS SDK or backend
 * @property message Human-readable error message
 * @property details Additional error details if available
 * @property cause The underlying cause of the error
 */
data class ThreeDSChallengeError(
    val code: String,
    val message: String,
    val details: String? = null,
    val cause: Throwable? = null,
)
