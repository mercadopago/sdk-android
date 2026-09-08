package com.mercadopago.sdk.android.analytics.domain.classifier

import androidx.annotation.RestrictTo
import com.mercadopago.sdk.android.analytics.domain.models.NativeError
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorCode
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorDiagnostic
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorOperation

/** The single Android policy owner for native-error classification. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class NativeErrorClassifier {
    /** Applies the canonical precedence and derives a delivery-ready native error. */
    @Suppress("CyclomaticComplexMethod")
    fun classify(operation: NativeErrorOperation, input: NativeErrorInput): NativeError {
        val (code, diagnostic) = when {
            input.type == NativeErrorType.USER_CANCELLATION ->
                NativeErrorCode.USER_CANCELLED to NativeErrorDiagnostic.CANCELLED
            input.type == NativeErrorType.REQUEST_CANCELLATION ||
                input.code == NativeErrorEvidenceCode.CANCELLED ->
                NativeErrorCode.REQUEST_CANCELLED to NativeErrorDiagnostic.CANCELLED
            input.code in CONFIGURATION_CODES || input.httpStatus in AUTH_STATUSES ->
                NativeErrorCode.SDK_CONFIGURATION_INVALID to input.configurationDiagnostic()
            input.type == NativeErrorType.VALIDATION ->
                NativeErrorCode.INPUT_VALIDATION_FAILED to NativeErrorDiagnostic.VALIDATION
            input.responseState != null || input.code == NativeErrorEvidenceCode.EMPTY_BODY ->
                NativeErrorCode.RESPONSE_CONTRACT_INVALID to
                    (input.responseState?.diagnostic ?: NativeErrorDiagnostic.EMPTY_BODY)
            input.code in CONNECTION_CODES ->
                NativeErrorCode.CONNECTION_UNAVAILABLE to input.code?.diagnostic
            input.code == NativeErrorEvidenceCode.TIMEOUT || input.httpStatus in TIMEOUT_STATUSES ->
                NativeErrorCode.REQUEST_TIMEOUT to NativeErrorDiagnostic.TIMEOUT
            input.type == NativeErrorType.REQUEST || input.type == NativeErrorType.SERVICE ->
                NativeErrorCode.UPSTREAM_REJECTED to null
            else -> NativeErrorCode.OPERATION_FAILED to null
        }
        return NativeError(
            operation = operation,
            code = code,
            statusCode = input.httpStatus,
            requestCorrelationId = input.requestCorrelationId,
            diagnostic = diagnostic,
        )
    }

    private fun NativeErrorInput.configurationDiagnostic() = when {
        code == NativeErrorEvidenceCode.INVALID_URL -> NativeErrorDiagnostic.INVALID_URL
        code == NativeErrorEvidenceCode.HTTP_UNAUTHORIZED || httpStatus == HTTP_UNAUTHORIZED ->
            NativeErrorDiagnostic.HTTP_UNAUTHORIZED
        code == NativeErrorEvidenceCode.HTTP_FORBIDDEN || httpStatus == HTTP_FORBIDDEN ->
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

    private companion object {
        val CONFIGURATION_CODES = setOf(
            NativeErrorEvidenceCode.CONFIGURATION,
            NativeErrorEvidenceCode.INTEGRATION,
            NativeErrorEvidenceCode.INVALID_URL,
            NativeErrorEvidenceCode.HTTP_UNAUTHORIZED,
            NativeErrorEvidenceCode.HTTP_FORBIDDEN,
        )
        val CONNECTION_CODES = setOf(
            NativeErrorEvidenceCode.OFFLINE,
            NativeErrorEvidenceCode.DNS_FAILURE,
            NativeErrorEvidenceCode.CONNECTION_LOST,
        )
        const val HTTP_UNAUTHORIZED = 401
        const val HTTP_FORBIDDEN = 403
        const val HTTP_REQUEST_TIMEOUT = 408
        const val HTTP_GATEWAY_TIMEOUT = 504
        val AUTH_STATUSES = setOf(HTTP_UNAUTHORIZED, HTTP_FORBIDDEN)
        val TIMEOUT_STATUSES = setOf(HTTP_REQUEST_TIMEOUT, HTTP_GATEWAY_TIMEOUT)
    }
}
