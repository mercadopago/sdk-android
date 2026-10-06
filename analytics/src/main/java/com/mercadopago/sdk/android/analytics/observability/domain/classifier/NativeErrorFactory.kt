package com.mercadopago.sdk.android.analytics.observability.domain.classifier

// @spec 20260825-native-coremethods-checkout-observability#DD-4

import androidx.annotation.RestrictTo
import com.mercadopago.sdk.android.analytics.observability.domain.models.NativeError
import com.mercadopago.sdk.android.analytics.observability.domain.models.NativeErrorOperation

/**
 * Creates canonical native errors from neutral SDK evidence.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class NativeErrorFactory {
    /**
     * Applies canonical classification precedence to neutral error evidence.
     *
     * @param operation SDK operation that failed.
     * @param input privacy-safe evidence collected by the operation adapter.
     * @return a delivery-ready native error with its canonical classification.
     */
    fun from(operation: NativeErrorOperation, input: NativeErrorInput): NativeError {
        val classification = input.classification()
        return NativeError(
            operation = operation,
            code = classification.code,
            statusCode = input.httpStatus,
            requestCorrelationId = input.requestCorrelationId,
            diagnostic = classification.diagnostic,
        )
    }
}
