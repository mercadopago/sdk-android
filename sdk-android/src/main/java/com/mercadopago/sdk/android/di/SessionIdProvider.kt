package com.mercadopago.sdk.android.di

import androidx.annotation.RestrictTo

/**
 * Provides the session identifier for the current SDK instance.
 *
 * Used internally to correlate requests and track session lifecycle within the SDK.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
interface SessionIdProvider {

    /**
     * Returns the unique session identifier for the current SDK instance.
     *
     * @return The session ID string.
     */
    fun getSessionId(): String
}
