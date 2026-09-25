package com.mercadopago.sdk.android.analytics.observability.domain.models

import androidx.annotation.RestrictTo

/**
 * Closed catalog of privacy-safe native error classifications.
 *
 * @property value stable wire value sent to the ingestion API.
 * @property category stable error category derived from the code.
 * @property critical whether the error is considered critical for observability.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
@Suppress("UndocumentedPublicProperty")
enum class NativeErrorCode(
    val value: String,
    val category: String,
    val critical: Boolean,
) {
    USER_CANCELLED("user_cancelled", "cancellation", false),
    REQUEST_CANCELLED("request_cancelled", "cancellation", false),
    INPUT_VALIDATION_FAILED("input_validation_failed", "input_validation", false),
    CONNECTION_UNAVAILABLE("connection_unavailable", "network", false),
    REQUEST_TIMEOUT("request_timeout", "service", true),
    UPSTREAM_REJECTED("upstream_rejected", "service", true),
    RESPONSE_CONTRACT_INVALID("response_contract_invalid", "integration", true),
    SDK_CONFIGURATION_INVALID("sdk_configuration_invalid", "integration", true),
    OPERATION_FAILED("operation_failed", "unknown", true),
}
