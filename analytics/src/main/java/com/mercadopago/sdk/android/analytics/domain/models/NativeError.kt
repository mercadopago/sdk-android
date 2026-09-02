@file:Suppress("UndocumentedPublicClass", "UndocumentedPublicProperty")

package com.mercadopago.sdk.android.analytics.domain.models

import androidx.annotation.RestrictTo

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
enum class NativeErrorModule(val value: String) {
    CORE_METHODS("core_methods"),
    CHECKOUT("checkout"),
}

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
enum class NativeErrorOperation(
    val value: String,
    val module: NativeErrorModule,
    val serviceTarget: String?,
) {
    IDENTIFICATION_TYPES("identification_types", NativeErrorModule.CORE_METHODS, "identification_types"),
    INSTALLMENTS("installments", NativeErrorModule.CORE_METHODS, "installments"),
    PAYMENT_METHODS("payment_methods", NativeErrorModule.CORE_METHODS, "payment_methods"),
    ISSUERS("issuers", NativeErrorModule.CORE_METHODS, "issuers"),
    CARD_TOKENIZATION("card_tokenization", NativeErrorModule.CORE_METHODS, null),
    CARD_FORM_INITIALIZATION("card_form_initialization", NativeErrorModule.CHECKOUT, "checkout_initialization"),
    CARD_FORM_SUBMISSION("card_form_submission", NativeErrorModule.CHECKOUT, null),
    CARD_FORM_CANCELLATION("card_form_cancellation", NativeErrorModule.CHECKOUT, null),
    INSTALLMENTS_CANCELLATION("installments_cancellation", NativeErrorModule.CHECKOUT, null),
    ORDER_SUBMISSION("order_submission", NativeErrorModule.CHECKOUT, "orders"),
}

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
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

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
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

internal enum class NativeErrorDeliveryMode {
    MELIDATA_ONLY,
    DUAL_WRITE,
    OBSERVABILITY_ONLY;

    companion object {
        fun from(value: String): NativeErrorDeliveryMode =
            values().firstOrNull { it.name == value } ?: MELIDATA_ONLY
    }
}

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
data class NativeError(
    val operation: NativeErrorOperation,
    val code: NativeErrorCode,
    val statusCode: Int? = null,
    val requestCorrelationId: String? = null,
    val diagnostic: NativeErrorDiagnostic? = null,
)

internal data class PendingNativeError(
    val eventId: String,
    val occurredAt: String,
    val error: NativeError,
)
