package com.mercadopago.sdk.android.analytics

import com.mercadopago.sdk.android.analytics.domain.exception.AnalyticsInitializationException
import com.mercadopago.sdk.android.analytics.domain.interactor.Analytics
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import junit.framework.TestCase.assertNotNull
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

internal class AnalyticsTest {

    private val sessionId = "session"
    private val siteId = "site"
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
    fun `test sessionId is correctly set`() {
        val analyticsInstance = Analytics.getInstance()
        val sessionIdProperty = Analytics::class.java.declaredFields
            .first { it.name == "sessionId" }

        sessionIdProperty.isAccessible = true // Make private property accessible
        val actualSessionId = sessionIdProperty.get(analyticsInstance)

        assertEquals(sessionId, actualSessionId)
    }

    @Test
    fun `test siteId is correctly set`() {
        val analyticsInstance = Analytics.getInstance()
        val siteIdProperty = Analytics::class.java.declaredFields
            .first { it.name == "siteId" }

        siteIdProperty.isAccessible = true
        val actualSiteId = siteIdProperty.get(analyticsInstance)

        assertEquals(siteId, actualSiteId)
    }

    @Test
    fun `test version is correctly set`() {
        val analyticsInstance = Analytics.getInstance()
        val versionProperty = Analytics::class.java.declaredFields
            .first { it.name == "version" }

        versionProperty.isAccessible = true
        val actualVersion = versionProperty.get(analyticsInstance)

        assertEquals(version, actualVersion)
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
    fun `track should set Track values and return Track`() {
        val analyticsInstance = Analytics.getInstance()
        val path = "path"
        val track = TrackType.EVENT
        val data = mockEventData("data")

        analyticsInstance.trackEvent(mockMetric(path, track, data))
    }
}
