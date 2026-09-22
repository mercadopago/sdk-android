package com.mercadopago.sdk.android.analytics.observability.domain.classifier

import androidx.annotation.RestrictTo

/**
 * Closed distinctions that product adapters may retain.
 *
 * @property value stable canonical value used by shared test vectors.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
@Suppress("UndocumentedPublicProperty")
enum class NativeErrorEvidenceCode(val value: String) {
    CANCELLED("cancelled"),
    CONFIGURATION("configuration"),
    INTEGRATION("integration"),
    INVALID_URL("invalid_url"),
    OFFLINE("offline"),
    DNS_FAILURE("dns_failure"),
    CONNECTION_LOST("connection_lost"),
    TIMEOUT("timeout"),
    EMPTY_BODY("empty_body"),
    EXCEPTION("exception"),
    UNKNOWN_ERROR("unknown_error"),
    HTTP_UNAUTHORIZED("http_unauthorized"),
    HTTP_FORBIDDEN("http_forbidden"),
}
