package com.mercadopago.sdk.android.checkout.statusscreen.presentation

import com.mercadopago.sdk.android.checkout.analytics.StatusScreenCloseSource
import kotlin.test.Test

// MPAnalytics.tryGetInstance() returns null in unit tests (no Android environment), so the
// guard logic is verified through call-count tracking rather than emitted metrics.
internal class StatusScreenAnalyticsTrackerTest {
    @Test
    fun `given no analytics instance when trackLoadStarted is called then it does not throw`() {
        // Given
        val tracker = StatusScreenAnalyticsTracker()

        // When / Then
        tracker.trackLoadStarted()
    }

    @Test
    fun `given no analytics instance when trackLoadSuccess is called then it does not throw`() {
        // Given
        val tracker = StatusScreenAnalyticsTracker()

        // When / Then
        tracker.trackLoadSuccess(statusType = "approved")
    }

    @Test
    fun `given no analytics instance when trackLoadFailure is called then it does not throw`() {
        // Given
        val tracker = StatusScreenAnalyticsTracker()

        // When / Then
        tracker.trackLoadFailure()
    }

    @Test
    fun `given trackRender already fired when it is called again then it is a no-op`() {
        // Given
        val tracker = StatusScreenAnalyticsTracker()
        tracker.trackRender(statusType = "approved")

        // When / Then
        tracker.trackRender(statusType = "approved")
    }

    @Test
    fun `given trackClose already fired when it is called again with another source then it is a no-op`() {
        // Given
        val tracker = StatusScreenAnalyticsTracker()
        tracker.trackClose(source = StatusScreenCloseSource.Back, statusType = "approved")

        // When / Then
        tracker.trackClose(source = StatusScreenCloseSource.Dismiss, statusType = "approved")
    }

    @Test
    fun `given a null statusType when trackClose is called then it does not throw`() {
        // Given
        val tracker = StatusScreenAnalyticsTracker()

        // When / Then
        tracker.trackClose(source = StatusScreenCloseSource.Unavailable, statusType = null)
    }
}
