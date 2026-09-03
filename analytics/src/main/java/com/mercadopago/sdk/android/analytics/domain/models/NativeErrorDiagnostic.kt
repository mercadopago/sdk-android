package com.mercadopago.sdk.android.analytics.domain.models

import androidx.annotation.RestrictTo

/** Closed catalog of optional, privacy-safe diagnostic details. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
enum class NativeErrorDiagnostic(
    /** Stable wire value sent to the ingestion API. */
    val value: String,
) {
    /** The operation was cancelled. */
    CANCELLED("cancelled"),
    /** Local validation rejected the input. */
    VALIDATION("validation"),
    /** The device was offline. */
    OFFLINE("offline"),
    /** DNS resolution failed. */
    DNS_FAILURE("dns_failure"),
    /** An established connection was lost. */
    CONNECTION_LOST("connection_lost"),
    /** The request exceeded its time limit. */
    TIMEOUT("timeout"),
    /** The service returned an empty body. */
    EMPTY_BODY("empty_body"),
    /** The response body could not be decoded. */
    DECODE_FAILURE("decode_failure"),
    /** The request URL could not be created. */
    INVALID_URL("invalid_url"),
    /** The service returned HTTP 401. */
    HTTP_UNAUTHORIZED("http_unauthorized"),
    /** The service returned HTTP 403. */
    HTTP_FORBIDDEN("http_forbidden"),
}
