package com.mercadopago.sdk.android.analytics.domain.models

import androidx.annotation.RestrictTo

/** Closed catalog of privacy-safe native error classifications. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
enum class NativeErrorCode(
    /** Stable wire value sent to the ingestion API. */
    val value: String,
    /** Stable error category derived from the code. */
    val category: String,
    /** Whether the error is considered critical for observability. */
    val critical: Boolean,
) {
    /** The buyer cancelled the flow. */
    USER_CANCELLED("user_cancelled", "cancellation", false),
    /** An in-flight request was cancelled. */
    REQUEST_CANCELLED("request_cancelled", "cancellation", false),
    /** Input validation failed before service execution. */
    INPUT_VALIDATION_FAILED("input_validation_failed", "input_validation", false),
    /** The network connection was unavailable. */
    CONNECTION_UNAVAILABLE("connection_unavailable", "network", false),
    /** A service request timed out. */
    REQUEST_TIMEOUT("request_timeout", "service", true),
    /** An upstream service rejected the request. */
    UPSTREAM_REJECTED("upstream_rejected", "service", true),
    /** The upstream response did not match its contract. */
    RESPONSE_CONTRACT_INVALID("response_contract_invalid", "integration", true),
    /** SDK configuration prevented the operation. */
    SDK_CONFIGURATION_INVALID("sdk_configuration_invalid", "integration", true),
    /** The operation failed without a more specific safe classification. */
    OPERATION_FAILED("operation_failed", "unknown", true),
}
