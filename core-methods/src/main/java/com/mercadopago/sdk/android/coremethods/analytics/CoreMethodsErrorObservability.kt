package com.mercadopago.sdk.android.coremethods.analytics

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.NativeError
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorCode
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorDiagnostic
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorOperation
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import java.util.Locale

internal class CoreMethodsErrorObservability(
    private val analyticsProvider: () -> MPAnalytics? = MPAnalytics::tryGetInstance,
) {
    fun track(
        error: ResultError,
        operation: NativeErrorOperation,
        legacyMetricFactory: (String) -> Metric,
    ) {
        try {
            analyticsProvider()?.trackError(
                error = NativeError(
                    operation = operation,
                    code = error.toNativeErrorCode(),
                    diagnostic = error.toDiagnostic(),
                ),
                legacyMetricFactory = legacyMetricFactory,
            )
        } catch (_: Throwable) {
            // Analytics availability never changes the original CoreMethods Result.
        }
    }

    internal fun ResultError.toNativeErrorCode(): NativeErrorCode =
        when (this) {
            is ResultError.Validation -> NativeErrorCode.INPUT_VALIDATION_FAILED
            is ResultError.Request -> when {
                code.uppercase(Locale.ROOT) in TIMEOUT_CODES -> NativeErrorCode.REQUEST_TIMEOUT
                code.uppercase(Locale.ROOT) in CONNECTION_CODES -> NativeErrorCode.CONNECTION_UNAVAILABLE
                code == EMPTY_BODY_STATUS && message == EMPTY_BODY_MESSAGE -> {
                    NativeErrorCode.RESPONSE_CONTRACT_INVALID
                }
                code in CONFIGURATION_STATUS_CODES -> NativeErrorCode.SDK_CONFIGURATION_INVALID
                code == UNKNOWN_ERROR_CODE -> NativeErrorCode.OPERATION_FAILED
                else -> NativeErrorCode.UPSTREAM_REJECTED
            }
        }

    private fun ResultError.toDiagnostic(): NativeErrorDiagnostic? =
        when (this) {
            is ResultError.Validation -> NativeErrorDiagnostic.VALIDATION
            is ResultError.Request -> when {
                code.uppercase(Locale.ROOT) in TIMEOUT_CODES -> NativeErrorDiagnostic.TIMEOUT
                code.uppercase(Locale.ROOT) in CONNECTION_CODES -> NativeErrorDiagnostic.OFFLINE
                code == EMPTY_BODY_STATUS && message == EMPTY_BODY_MESSAGE -> NativeErrorDiagnostic.EMPTY_BODY
                code == "401" -> NativeErrorDiagnostic.HTTP_UNAUTHORIZED
                code == "403" -> NativeErrorDiagnostic.HTTP_FORBIDDEN
                else -> null
            }
        }

    private companion object {
        const val EMPTY_BODY_STATUS = "200"
        const val EMPTY_BODY_MESSAGE = "empty body"
        const val UNKNOWN_ERROR_CODE = "UNKNOWN_ERROR"
        val TIMEOUT_CODES = setOf("TIMEOUT", "NETWORK_TIMEOUT", "408", "504")
        val CONNECTION_CODES = setOf("NETWORK", "CONNECTION", "NO_INTERNET", "UNREACHABLE")
        val CONFIGURATION_STATUS_CODES = setOf("401", "403")
    }
}
