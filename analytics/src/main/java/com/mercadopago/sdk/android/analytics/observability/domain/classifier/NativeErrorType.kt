package com.mercadopago.sdk.android.analytics.observability.domain.classifier

import androidx.annotation.RestrictTo

/**
 * Origin of neutral failure evidence.
 *
 * @property value stable canonical value used by shared test vectors.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
@Suppress("UndocumentedPublicProperty")
enum class NativeErrorType(val value: String) {
    REQUEST("request"),
    SERVICE("service"),
    VALIDATION("validation"),
    USER_CANCELLATION("user_cancellation"),
    REQUEST_CANCELLATION("request_cancellation"),
    UNKNOWN("unknown"),
}
