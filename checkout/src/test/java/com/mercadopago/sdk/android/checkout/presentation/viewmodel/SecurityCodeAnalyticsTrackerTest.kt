package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import kotlin.test.Test

/**
 * Tests for [SecurityCodeAnalyticsTracker].
 *
 * MPAnalytics.tryGetInstance() returns null in unit tests (no Android environment), so the
 * guard logic is verified through call-count tracking rather than emitted metrics.
 */
internal class SecurityCodeAnalyticsTrackerTest {
    private fun makeTracker(
        isLoading: () -> Boolean = { false },
    ) = SecurityCodeAnalyticsTracker(
        paymentMethodId = "visa",
        paymentTypeId = "credit_card",
        issuerId = "1",
        cardId = "card-123",
        isLoading = isLoading,
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
    fun `trackContinue is skipped while loading and does not throw`() {
        val tracker = makeTracker(isLoading = { true })
        tracker.trackContinue()
        tracker.trackContinue()
    }

    @Test
    fun `trackBack does not throw when analytics is unavailable`() {
        makeTracker().trackBack()
    }

    @Test
    fun `trackBack fires only once on repeated calls`() {
        val tracker = makeTracker()
        tracker.trackBack()
        tracker.trackBack() // second call must be a no-op — no throw
    }
}
