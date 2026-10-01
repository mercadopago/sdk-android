package com.mercadopago.sdk.android.analytics.observability.domain.classifier

import androidx.annotation.RestrictTo

/**
 * Closed, privacy-safe evidence used to classify a native SDK error.
 *
 * @property type closed origin of the failure.
 * @property code optional closed evidence needed to distinguish classifications.
 * @property httpStatus validated HTTP status retained only when reliable.
 * @property responseState optional closed response-contract evidence.
 * @property requestCorrelationId optional allowlisted request correlation identifier.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class NativeErrorInput private constructor(
    val type: NativeErrorType,
    val code: NativeErrorEvidenceCode?,
    val httpStatus: Int?,
    val responseState: NativeErrorResponseState?,
    val requestCorrelationId: String?,
) {
    /** Validated construction boundary for neutral evidence. */
    companion object {
        /** Creates evidence while discarding optional values outside the public contract. */
        fun create(
            type: NativeErrorType,
            code: NativeErrorEvidenceCode? = null,
            httpStatus: Int? = null,
            responseState: NativeErrorResponseState? = null,
            requestCorrelationId: String? = null,
        ) = NativeErrorInput(
            type = type,
            code = code,
            httpStatus = httpStatus?.validNativeHttpStatus(),
            responseState = responseState,
            requestCorrelationId = requestCorrelationId?.validNativeCorrelationId(),
        )
    }
}
