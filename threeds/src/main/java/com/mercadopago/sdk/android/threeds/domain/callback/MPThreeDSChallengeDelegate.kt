package com.mercadopago.sdk.android.threeds.domain.callback

import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticated
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeError

/**
 * Delegate interface for handling 3DS challenge flow callbacks.
 * Implement this interface to receive callbacks during the 3DS authentication process.
 */
interface MPThreeDSChallengeDelegate {

    /**
     * Called when the 3DS authentication completes successfully.
     *
     * @param result The authentication result containing response data
     */
    fun onSuccess(result: MPThreeDSAuthenticated)

    /**
     * Called when the 3DS authentication fails or encounters an error.
     *
     * @param error The error that occurred during authentication
     */
    fun onError(error: MPThreeDSChallengeError)

    /**
     * Called when the user cancels the 3DS challenge.
     */
    fun onCancel()
    /**
     * Called when timedOut the 3DS challenge.
     */
    fun onTimedOut()
}
