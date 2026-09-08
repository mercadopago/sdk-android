package com.mercadopago.sdk.android.analytics.domain.classifier

import androidx.annotation.RestrictTo

/** Closed, privacy-safe evidence used to classify a native SDK error. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class NativeErrorInput private constructor(
    /** Closed origin of the failure. */
    val type: NativeErrorType,
    /** Optional closed evidence needed to distinguish classifications. */
    val code: NativeErrorEvidenceCode?,
    /** Validated HTTP status retained only when reliable. */
    val httpStatus: Int?,
    /** Optional closed response-contract evidence. */
    val responseState: NativeErrorResponseState?,
    /** Optional allowlisted request correlation identifier. */
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
            httpStatus = httpStatus?.takeIf { it in MIN_HTTP_STATUS..MAX_HTTP_STATUS },
            responseState = responseState,
            requestCorrelationId = requestCorrelationId?.takeIf {
                it.length in 1..MAX_CORRELATION_LENGTH && CORRELATION_REGEX.matches(it)
            },
        )

        private val CORRELATION_REGEX = Regex("[A-Za-z0-9._:-]+")
        private const val MIN_HTTP_STATUS = 100
        private const val MAX_HTTP_STATUS = 599
        private const val MAX_CORRELATION_LENGTH = 128
    }
}

/** Origin of the neutral failure evidence. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
enum class NativeErrorType(
    /** Stable canonical value used by shared test vectors. */
    val value: String,
) {
    /** Request-layer failure. */
    REQUEST("request"),
    /** Service rejection. */
    SERVICE("service"),
    /** Local validation failure. */
    VALIDATION("validation"),
    /** Explicit buyer cancellation. */
    USER_CANCELLATION("user_cancellation"),
    /** In-flight request cancellation. */
    REQUEST_CANCELLATION("request_cancellation"),
    /** Unclassified failure. */
    UNKNOWN("unknown"),
}

/** Closed distinctions that product adapters may retain. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
enum class NativeErrorEvidenceCode(
    /** Stable canonical value used by shared test vectors. */
    val value: String,
) {
    /** Cancellation evidence. */
    CANCELLED("cancelled"),
    /** Configuration evidence. */
    CONFIGURATION("configuration"),
    /** Integration evidence. */
    INTEGRATION("integration"),
    /** Invalid URL evidence. */
    INVALID_URL("invalid_url"),
    /** Offline evidence. */
    OFFLINE("offline"),
    /** DNS failure evidence. */
    DNS_FAILURE("dns_failure"),
    /** Lost connection evidence. */
    CONNECTION_LOST("connection_lost"),
    /** Timeout evidence. */
    TIMEOUT("timeout"),
    /** Empty response evidence. */
    EMPTY_BODY("empty_body"),
    /** Exception fallback evidence. */
    EXCEPTION("exception"),
    /** Unknown error evidence. */
    UNKNOWN_ERROR("unknown_error"),
    /** HTTP unauthorized evidence. */
    HTTP_UNAUTHORIZED("http_unauthorized"),
    /** HTTP forbidden evidence. */
    HTTP_FORBIDDEN("http_forbidden"),
}

/** Closed response-contract evidence. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
enum class NativeErrorResponseState(
    /** Stable canonical value used by shared test vectors. */
    val value: String,
) {
    /** Empty body response. */
    EMPTY_BODY("empty_body"),
    /** Response decoding failure. */
    DECODE_FAILURE("decode_failure"),
}
