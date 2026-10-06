package com.mercadopago.sdk.android.analytics.observability.domain.classifier

// @spec 20260825-native-coremethods-checkout-observability#DD-4

import com.mercadopago.sdk.android.analytics.observability.domain.models.NativeErrorCode
import com.mercadopago.sdk.android.analytics.observability.domain.models.NativeErrorDiagnostic

private val AUTH_STATUSES = setOf(
    NativeErrorHttpStatusCodes.UNAUTHORIZED,
    NativeErrorHttpStatusCodes.FORBIDDEN,
)
private val TIMEOUT_STATUSES = setOf(
    NativeErrorHttpStatusCodes.REQUEST_TIMEOUT,
    NativeErrorHttpStatusCodes.GATEWAY_TIMEOUT,
)
private val CONFIGURATION_CODES = setOf(
    NativeErrorEvidenceCode.CONFIGURATION,
    NativeErrorEvidenceCode.INTEGRATION,
    NativeErrorEvidenceCode.INVALID_URL,
    NativeErrorEvidenceCode.HTTP_UNAUTHORIZED,
    NativeErrorEvidenceCode.HTTP_FORBIDDEN,
)
private val CONNECTION_CODES = setOf(
    NativeErrorEvidenceCode.OFFLINE,
    NativeErrorEvidenceCode.DNS_FAILURE,
    NativeErrorEvidenceCode.CONNECTION_LOST,
)

internal data class NativeErrorClassification(
    val code: NativeErrorCode,
    val diagnostic: NativeErrorDiagnostic? = null,
)

internal fun NativeErrorInput.classification(): NativeErrorClassification =
    cancellationClassification()
        ?: configurationClassification()
        ?: validationClassification()
        ?: responseClassification()
        ?: connectionClassification()
        ?: timeoutClassification()
        ?: upstreamClassification()
        ?: NativeErrorClassification(NativeErrorCode.OPERATION_FAILED)

private fun NativeErrorInput.cancellationClassification(): NativeErrorClassification? = when {
    type == NativeErrorType.USER_CANCELLATION ->
        NativeErrorClassification(NativeErrorCode.USER_CANCELLED, NativeErrorDiagnostic.CANCELLED)
    type == NativeErrorType.REQUEST_CANCELLATION || code == NativeErrorEvidenceCode.CANCELLED ->
        NativeErrorClassification(NativeErrorCode.REQUEST_CANCELLED, NativeErrorDiagnostic.CANCELLED)
    else -> null
}

private fun NativeErrorInput.configurationClassification(): NativeErrorClassification? =
    NativeErrorClassification(NativeErrorCode.SDK_CONFIGURATION_INVALID, configurationDiagnostic())
        .takeIf { code in CONFIGURATION_CODES || httpStatus in AUTH_STATUSES }

private fun NativeErrorInput.validationClassification(): NativeErrorClassification? =
    NativeErrorClassification(NativeErrorCode.INPUT_VALIDATION_FAILED, NativeErrorDiagnostic.VALIDATION)
        .takeIf { type == NativeErrorType.VALIDATION }

private fun NativeErrorInput.responseClassification(): NativeErrorClassification? =
    NativeErrorClassification(
        NativeErrorCode.RESPONSE_CONTRACT_INVALID,
        responseState?.diagnostic ?: NativeErrorDiagnostic.EMPTY_BODY,
    ).takeIf { responseState != null || code == NativeErrorEvidenceCode.EMPTY_BODY }

private fun NativeErrorInput.connectionClassification(): NativeErrorClassification? =
    NativeErrorClassification(NativeErrorCode.CONNECTION_UNAVAILABLE, code?.diagnostic)
        .takeIf { code in CONNECTION_CODES }

private fun NativeErrorInput.timeoutClassification(): NativeErrorClassification? =
    NativeErrorClassification(NativeErrorCode.REQUEST_TIMEOUT, NativeErrorDiagnostic.TIMEOUT)
        .takeIf { code == NativeErrorEvidenceCode.TIMEOUT || httpStatus in TIMEOUT_STATUSES }

private fun NativeErrorInput.upstreamClassification(): NativeErrorClassification? =
    NativeErrorClassification(NativeErrorCode.UPSTREAM_REJECTED)
        .takeIf { type == NativeErrorType.REQUEST || type == NativeErrorType.SERVICE }

private fun NativeErrorInput.configurationDiagnostic() = when {
    code == NativeErrorEvidenceCode.INVALID_URL -> NativeErrorDiagnostic.INVALID_URL
    code == NativeErrorEvidenceCode.HTTP_UNAUTHORIZED || httpStatus == NativeErrorHttpStatusCodes.UNAUTHORIZED ->
        NativeErrorDiagnostic.HTTP_UNAUTHORIZED
    code == NativeErrorEvidenceCode.HTTP_FORBIDDEN || httpStatus == NativeErrorHttpStatusCodes.FORBIDDEN ->
        NativeErrorDiagnostic.HTTP_FORBIDDEN
    else -> null
}

private val NativeErrorResponseState.diagnostic get() = when (this) {
    NativeErrorResponseState.EMPTY_BODY -> NativeErrorDiagnostic.EMPTY_BODY
    NativeErrorResponseState.DECODE_FAILURE -> NativeErrorDiagnostic.DECODE_FAILURE
}

private val NativeErrorEvidenceCode.diagnostic get() = when (this) {
    NativeErrorEvidenceCode.OFFLINE -> NativeErrorDiagnostic.OFFLINE
    NativeErrorEvidenceCode.DNS_FAILURE -> NativeErrorDiagnostic.DNS_FAILURE
    NativeErrorEvidenceCode.CONNECTION_LOST -> NativeErrorDiagnostic.CONNECTION_LOST
    else -> null
}
