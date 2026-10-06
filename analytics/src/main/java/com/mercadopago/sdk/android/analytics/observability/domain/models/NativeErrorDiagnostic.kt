package com.mercadopago.sdk.android.analytics.observability.domain.models

import androidx.annotation.RestrictTo

/**
 * Closed catalog of optional, privacy-safe diagnostic details.
 *
 * @property value stable wire value sent to the ingestion API.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
@Suppress("UndocumentedPublicProperty")
enum class NativeErrorDiagnostic(val value: String) {
    CANCELLED("cancelled"),
    VALIDATION("validation"),
    OFFLINE("offline"),
    DNS_FAILURE("dns_failure"),
    CONNECTION_LOST("connection_lost"),
    TIMEOUT("timeout"),
    EMPTY_BODY("empty_body"),
    DECODE_FAILURE("decode_failure"),
    INVALID_URL("invalid_url"),
    HTTP_UNAUTHORIZED("http_unauthorized"),
    HTTP_FORBIDDEN("http_forbidden"),
}
