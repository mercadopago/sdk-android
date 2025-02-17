package com.mercadopago.sdk.android.analytics

import com.mercadopago.sdk.android.analytics.domain.exception.AnalyticsInitializationException
import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

internal class MPAnalyticsTest {

    private val sessionId = "session"
    private val siteId = "site"
    private val version = "1.0"
    private val getSiteIdFlow = flowOf<String>()

    @Before
    fun setup() {
        MPAnalytics.initialize(sessionId, siteId, version, getSiteIdFlow)
    }

    @Test
    fun `test initialize Analytics`() {
        val analytics = MPAnalytics.getInstance()
        assertNotNull(analytics)
    }

    @Test
    fun `test sessionId is correctly set`() {
        val analyticsInstance = MPAnalytics.getInstance()
        val sessionIdProperty = MPAnalytics::class.java.declaredFields
            .first { it.name == "sessionId" }

        sessionIdProperty.isAccessible = true // Make private property accessible
        val actualSessionId = sessionIdProperty.get(analyticsInstance)

        assertEquals(sessionId, actualSessionId)
    }

    @Test
    fun `test version is correctly set`() {
        val analyticsInstance = MPAnalytics.getInstance()
        val versionProperty = MPAnalytics::class.java.declaredFields
            .first { it.name == "version" }

        versionProperty.isAccessible = true
        val actualVersion = versionProperty.get(analyticsInstance)

        assertEquals(version, actualVersion)
    }

    @Test
    fun `test getInstance throws exception before initialization`() {
        // Reset Analytics instance to simulate being uninitialized
        MPAnalytics.initialize(sessionId, siteId, version, getSiteIdFlow)

        // Reinitialize with a new instance, and then reset to null to throw exception
        MPAnalytics::class.java.getDeclaredField("instance").apply {
            isAccessible = true
            set(null, null)
        }

        assertThrows(AnalyticsInitializationException::class.java) {
            MPAnalytics.getInstance()
        }
    }
}
