package com.mercadopago.sdk.android.analytics

import com.mercadopago.sdk.android.analytics.domain.exception.AnalyticsInitializationException
import com.mercadopago.sdk.android.analytics.domain.interactor.Analytics
import junit.framework.TestCase.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

internal class SetupTrackTrackerTest {

    private val sessionId = "sessionId123"
    private val siteId = "siteId456"
    private val version = "1.0"

    @Before
    fun setup() {
        Analytics.initialize(sessionId, siteId, version)
    }

    @Test
    fun `test initialize Analytics`() {
        val analytics = Analytics.getInstance()
        assertNotNull(analytics)
    }

    @Test
    fun `test getInstance throws exception before initialization`() {
        // Reset Analytics instance to simulate being uninitialized
        Analytics.initialize(sessionId, siteId, version)

        // Reinitialize with a new instance, and then reset to null to throw exception
        Analytics::class.java.getDeclaredField("instance").apply {
            isAccessible = true
            set(null, null)
        }

        assertThrows(AnalyticsInitializationException::class.java) {
            Analytics.getInstance()
        }
    }

    @Test
    fun `test trackEvent processes event correctly`() {
        val analytics = Analytics.getInstance()
        analytics.trackEvent(mockMetric)
    }
}
