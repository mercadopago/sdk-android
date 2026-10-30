package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.checkout.analytics.metricSecurityCodeBack
import com.mercadopago.sdk.android.checkout.analytics.metricSecurityCodeContinue
import com.mercadopago.sdk.android.checkout.analytics.metricSecurityCodeContinueError
import com.mercadopago.sdk.android.checkout.analytics.metricSecurityCodeView

/**
 * Tracks Melidata events for the CVV (security code) screen.
 *
 * Events:
 * - [trackView] → fired once when the screen is displayed
 * - [trackContinue] → fired when the user taps "Continuar"; guarded by [isLoading] to drop
 *   duplicate events from rapid double-taps before the loading state propagates
 * - [trackBack] → fired when the user navigates back / cancels; idempotent — only the first
 *   call fires the event (subsequent calls are no-ops)
 */
internal class SecurityCodeAnalyticsTracker(
    private val paymentMethodId: String,
    private val paymentTypeId: String,
    private val issuerId: String,
    private val cardId: String,
    private val isLoading: () -> Boolean = { false },
) {
    private var cancelTracked = false

    fun trackView() {
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricSecurityCodeView(
                paymentMethodId = paymentMethodId,
                paymentTypeId = paymentTypeId,
                issuerId = issuerId,
                cardId = cardId,
            ),
        )
    }

    fun trackContinue() {
        if (isLoading()) return
        MPAnalytics.tryGetInstance()?.trackMetric(metricSecurityCodeContinue())
    }

    fun trackContinueError(
        errorType: String,
    ) {
        MPAnalytics.tryGetInstance()?.trackMetric(metricSecurityCodeContinueError(errorType))
    }

    fun trackBack() {
        if (cancelTracked) return
        cancelTracked = true
        MPAnalytics.tryGetInstance()?.trackMetric(metricSecurityCodeBack())
    }
}
