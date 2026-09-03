package com.mercadopago.sdk.android.analytics.domain.models

import androidx.annotation.RestrictTo

/** A privacy-safe native SDK error ready for classification and delivery. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
data class NativeError(
    /** SDK operation that failed. */
    val operation: NativeErrorOperation,
    /** Stable, privacy-safe error classification. */
    val code: NativeErrorCode,
    /** Valid HTTP status code when one is available. */
    val statusCode: Int? = null,
    /** Allowlisted request identifier used only for correlation. */
    val requestCorrelationId: String? = null,
    /** Optional diagnostic selected from the closed catalog. */
    val diagnostic: NativeErrorDiagnostic? = null,
)

/** Internal envelope captured before the error is dispatched asynchronously. */
internal data class PendingNativeError(
    /** Unique identifier shared with the related Melidata event. */
    val eventId: String,
    /** ISO-8601 timestamp captured on the caller path. */
    val occurredAt: String,
    /** Classified error payload. */
    val error: NativeError,
)
