package com.mercadopago.sdk.android.checkout.analytics

import com.mercadopago.sdk.android.analytics.domain.classifier.NativeErrorInput
import com.mercadopago.sdk.android.analytics.domain.classifier.NativeErrorType
import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.analytics.domain.models.Metric
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
        send(operation, error.nativeErrorInput, legacyMetricFactory)
    }

    fun trackCancellation(
        operation: NativeErrorOperation,
        legacyMetricFactory: (String) -> Metric,
    ) {
        send(operation, NativeErrorInput.create(NativeErrorType.USER_CANCELLATION), legacyMetricFactory)
    }

    private fun send(
        operation: NativeErrorOperation,
        input: NativeErrorInput,
        legacyMetricFactory: (String) -> Metric,
    ) {
        try {
            analyticsProvider()?.trackError(operation, input, legacyMetricFactory)
        } catch (_: Throwable) {
            // Reporting cannot change Checkout callbacks or public errors.
        }
    }
}
