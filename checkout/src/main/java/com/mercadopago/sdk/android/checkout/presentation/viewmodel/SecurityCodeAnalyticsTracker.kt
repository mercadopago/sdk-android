package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.checkout.analytics.metricSecurityCodeBack
import com.mercadopago.sdk.android.checkout.analytics.metricSecurityCodeContinue
import com.mercadopago.sdk.android.checkout.analytics.metricSecurityCodeView

/**
 * Tracks Melidata events for the CVV (security code) screen.
 *
 * Events:
 * - [trackView] → fired once when the screen is displayed
 * - [trackContinue] → fired when the user taps "Continuar"
 * - [trackBack] → fired when the user navigates back / cancels
 */
internal class SecurityCodeAnalyticsTracker(
    private val paymentMethodId: String,
    private val paymentTypeId: String,
    private val issuerId: String,
    private val cardId: String,
) {
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
        MPAnalytics.tryGetInstance()?.trackMetric(metricSecurityCodeContinue())
    }

    fun trackBack() {
        MPAnalytics.tryGetInstance()?.trackMetric(metricSecurityCodeBack())
    }
}
