package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import kotlin.test.Test

/**
 * Smoke tests for [SecurityCodeAnalyticsTracker].
 *
 * MPAnalytics.tryGetInstance() returns null in unit tests (no Android environment), so we
 * verify the tracker methods don't throw when the analytics engine is unavailable — the same
 * behaviour as all other analytics trackers in this module.
 */
internal class SecurityCodeAnalyticsTrackerTest {
    private fun makeTracker() = SecurityCodeAnalyticsTracker(
        paymentMethodId = "visa",
        paymentTypeId = "credit_card",
        issuerId = "1",
        cardId = "card-123",
    )

    @Test
    fun `trackView does not throw when analytics is unavailable`() {
        makeTracker().trackView()
    }

    @Test
    fun `trackContinue does not throw when analytics is unavailable`() {
        makeTracker().trackContinue()
    }

    @Test
    fun `trackBack does not throw when analytics is unavailable`() {
        makeTracker().trackBack()
    }
}
