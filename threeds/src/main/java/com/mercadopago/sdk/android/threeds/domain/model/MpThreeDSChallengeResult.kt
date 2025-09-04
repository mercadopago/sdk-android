package com.mercadopago.sdk.android.threeds.domain.model
/**
 * Implement this interface to receive callbacks during the 3DS authentication process.
 */
sealed class MPThreeDSChallengeResult {
    /**
     * Called when the 3DS authentication completes successfully.
     *
     * @param result The authentication result containing response data
     */
    data class OnSuccess(val result: MPThreeDSAuthenticated) : MPThreeDSChallengeResult()

    /**
     * Called when the 3DS authentication fails or encounters an error.
     *
     * @param error The error that occurred during authentication
     */
    data class OnError(val error: MPThreeDSChallengeError) : MPThreeDSChallengeResult()

    /**
     * Called when the user cancels the 3DS challenge.
     */
    data object OnCancel : MPThreeDSChallengeResult()

    /**
     * Called when timedOut the 3DS challenge.
     */
    data object OnTimedOut : MPThreeDSChallengeResult()
}
