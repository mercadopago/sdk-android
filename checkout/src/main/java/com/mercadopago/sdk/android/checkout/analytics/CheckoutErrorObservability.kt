package com.mercadopago.sdk.android.checkout.analytics

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.NativeError
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorCode
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorDiagnostic
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorOperation
import com.mercadopago.sdk.android.checkout.domain.model.ObservedCheckoutError

internal class CheckoutErrorObservability(
    private val analyticsProvider: () -> MPAnalytics? = MPAnalytics::tryGetInstance,
) {
    fun track(
        error: ObservedCheckoutError,
        operation: NativeErrorOperation,
        legacyMetricFactory: (String) -> Metric,
    ) {
        send(
            NativeError(
                operation = operation,
                code = error.nativeCode,
                statusCode = error.httpStatus,
                diagnostic = error.diagnostic,
            ),
            legacyMetricFactory,
        )
    }

    fun trackCancellation(
        operation: NativeErrorOperation,
        legacyMetricFactory: (String) -> Metric,
    ) {
        send(
            NativeError(
                operation = operation,
                code = NativeErrorCode.USER_CANCELLED,
                diagnostic = NativeErrorDiagnostic.CANCELLED,
            ),
            legacyMetricFactory,
        )
    }

    private fun send(
        error: NativeError,
        legacyMetricFactory: (String) -> Metric,
    ) {
        try {
            analyticsProvider()?.trackError(error, legacyMetricFactory)
        } catch (_: Throwable) {
            // Reporting cannot change Checkout callbacks or public errors.
        }
    }
}
