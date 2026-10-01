package com.mercadopago.sdk.android.coremethods.analytics

// @spec 20260825-native-coremethods-checkout-observability#DD-4

import com.mercadopago.sdk.android.analytics.observability.domain.classifier.NativeErrorEvidenceCode
import com.mercadopago.sdk.android.analytics.observability.domain.classifier.NativeErrorHttpStatusCodes
import com.mercadopago.sdk.android.analytics.observability.domain.classifier.NativeErrorInput
import com.mercadopago.sdk.android.analytics.observability.domain.classifier.NativeErrorResponseState
import com.mercadopago.sdk.android.analytics.observability.domain.classifier.NativeErrorType
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import java.util.Locale

internal class CoreMethodsErrorObservability {
    fun input(
        error: ResultError,
    ): NativeErrorInput = error.toNativeErrorInput()

    private fun ResultError.toNativeErrorInput(): NativeErrorInput =
        when (this) {
            is ResultError.Validation -> NativeErrorInput.create(NativeErrorType.VALIDATION)
            is ResultError.Request -> when {
                normalizedCode in TIMEOUT_CODES -> request(NativeErrorEvidenceCode.TIMEOUT)
                normalizedCode in CONNECTION_CODES -> request(NativeErrorEvidenceCode.OFFLINE)
                code == EMPTY_BODY_STATUS && message == EMPTY_BODY_MESSAGE -> NativeErrorInput.create(
                    type = NativeErrorType.REQUEST,
                    responseState = NativeErrorResponseState.EMPTY_BODY,
                )
                normalizedCode.toIntOrNull() == NativeErrorHttpStatusCodes.UNAUTHORIZED ->
                    request(NativeErrorEvidenceCode.HTTP_UNAUTHORIZED)
                normalizedCode.toIntOrNull() == NativeErrorHttpStatusCodes.FORBIDDEN ->
                    request(NativeErrorEvidenceCode.HTTP_FORBIDDEN)
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
        val TIMEOUT_CODES = setOf(
            "TIMEOUT",
            "NETWORK_TIMEOUT",
            NativeErrorHttpStatusCodes.REQUEST_TIMEOUT.toString(),
            NativeErrorHttpStatusCodes.GATEWAY_TIMEOUT.toString(),
        )
        val CONNECTION_CODES = setOf("NETWORK", "CONNECTION", "NO_INTERNET", "UNREACHABLE")
    }
}
