package com.mercadopago.sdk.android.di

import androidx.annotation.RestrictTo

/**
 * Provides the session identifier for the current SDK instance.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
interface SessionIdProvider {

    fun getSessionId(): String
}
