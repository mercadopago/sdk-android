package com.mercadopago.sdk.android.analytics.observability.domain.models

import androidx.annotation.RestrictTo

/**
 * A privacy-safe native SDK error ready for classification and delivery.
 *
 * @param operation SDK operation that failed.
 * @param code stable, privacy-safe error classification.
 * @param statusCode valid HTTP status code when one is available.
 * @param requestCorrelationId allowlisted request identifier used only for correlation.
 * @param diagnostic optional diagnostic selected from the closed catalog.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
data class NativeError(
    val operation: NativeErrorOperation,
    val code: NativeErrorCode,
    val statusCode: Int? = null,
    val requestCorrelationId: String? = null,
    val diagnostic: NativeErrorDiagnostic? = null,
)
