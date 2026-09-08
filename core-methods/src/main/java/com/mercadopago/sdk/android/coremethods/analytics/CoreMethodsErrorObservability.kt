package com.mercadopago.sdk.android.coremethods.analytics

import com.mercadopago.sdk.android.analytics.domain.classifier.NativeErrorEvidenceCode
import com.mercadopago.sdk.android.analytics.domain.classifier.NativeErrorInput
import com.mercadopago.sdk.android.analytics.domain.classifier.NativeErrorResponseState
import com.mercadopago.sdk.android.analytics.domain.classifier.NativeErrorType
import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.analytics.domain.models.Metric
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
                operation = operation,
                input = error.toNativeErrorInput(),
                legacyMetricFactory = legacyMetricFactory,
            )
        } catch (_: Throwable) {
            // Analytics availability never changes the original CoreMethods Result.
        }
    }

    internal fun ResultError.toNativeErrorInput(): NativeErrorInput =
        when (this) {
            is ResultError.Validation -> NativeErrorInput.create(NativeErrorType.VALIDATION)
            is ResultError.Request -> when {
                normalizedCode in TIMEOUT_CODES -> request(NativeErrorEvidenceCode.TIMEOUT)
                normalizedCode in CONNECTION_CODES -> request(NativeErrorEvidenceCode.OFFLINE)
                code == EMPTY_BODY_STATUS && message == EMPTY_BODY_MESSAGE -> NativeErrorInput.create(
                    type = NativeErrorType.REQUEST,
                    responseState = NativeErrorResponseState.EMPTY_BODY,
                )
                normalizedCode == HTTP_UNAUTHORIZED -> request(NativeErrorEvidenceCode.HTTP_UNAUTHORIZED)
                normalizedCode == HTTP_FORBIDDEN -> request(NativeErrorEvidenceCode.HTTP_FORBIDDEN)
                normalizedCode == UNKNOWN_ERROR_CODE -> NativeErrorInput.create(
                    type = NativeErrorType.UNKNOWN,
                    code = NativeErrorEvidenceCode.UNKNOWN_ERROR,
                )
                else -> request()
            }
        }

    private val ResultError.Request.normalizedCode get() = code.uppercase(Locale.ROOT)

    private fun request(
        code: NativeErrorEvidenceCode? = null,
    ) = NativeErrorInput.create(
        type = NativeErrorType.REQUEST,
        code = code,
    )

    private companion object {
        const val EMPTY_BODY_STATUS = "200"
        const val EMPTY_BODY_MESSAGE = "empty body"
        const val UNKNOWN_ERROR_CODE = "UNKNOWN_ERROR"
        const val HTTP_UNAUTHORIZED = "401"
        const val HTTP_FORBIDDEN = "403"
        val TIMEOUT_CODES = setOf("TIMEOUT", "NETWORK_TIMEOUT", "408", "504")
        val CONNECTION_CODES = setOf("NETWORK", "CONNECTION", "NO_INTERNET", "UNREACHABLE")
    }
}
