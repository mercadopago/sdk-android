package com.mercadopago.sdk.android.analytics.observability.domain.classifier

// @spec 20260825-native-coremethods-checkout-observability#DD-4

import androidx.annotation.RestrictTo

/**
 * Canonical HTTP status codes used by native-error adapters and classification.
 *
 * This object is library-group restricted because CoreMethods and Checkout are
 * separate Gradle modules that share the same closed classification contract.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
object NativeErrorHttpStatusCodes {
    /** Lowest status accepted as a valid HTTP response code. */
    const val MIN_VALID = 100

    /** Highest status accepted as a valid HTTP response code. */
    const val MAX_VALID = 599

    /** HTTP 401 Unauthorized. */
    const val UNAUTHORIZED = 401

    /** HTTP 403 Forbidden. */
    const val FORBIDDEN = 403

    /** HTTP 408 Request Timeout. */
    const val REQUEST_TIMEOUT = 408

    /** HTTP 504 Gateway Timeout. */
    const val GATEWAY_TIMEOUT = 504
}
