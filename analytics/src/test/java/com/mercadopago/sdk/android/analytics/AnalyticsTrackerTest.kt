package com.mercadopago.sdk.android.analytics

import com.mercadopago.sdk.android.analytics.models.Application
import com.mercadopago.sdk.android.analytics.models.Device
import com.mercadopago.sdk.android.analytics.models.Track
import com.mercadopago.sdk.android.analytics.models.User
import junit.framework.TestCase.assertSame
import org.junit.Test

class AnalyticsTrackerTest {

    @Test
    fun `trackEvent should set TrackType to event and return self`() {
        val analytics = Analytics()
        val result = analytics.trackEvent("payment/credit_card")
        assertSame(analytics, result)
    }

    @Test
    fun `trackView should set TrackType to view and return self`() {
        val analytics = Analytics()
        val result = analytics.trackView("checkout/review")
        assertSame(analytics, result)
    }

    @Test
    fun `track should set Track values and return Track`() {
        val eventData = AnalyticsEventData()
        val analytics = Analytics()
            .setVersion("1.0.0")
            .trackView("payment/credit_card")
            .setEventData(eventData)
            .setSiteId("MLB")

        val result = analytics.send()
        val expected = Track(
            path = "payment/credit_card",
            user = User(
                uid = ""
            ),
            type = TrackType.VIEW.name,
            id = "",
            userTime = System.currentTimeMillis().toString(),
            eventData = eventData,
            application = Application(
                business = "mercadopago",
                siteId = "MLB",
                version = "1.0.0"
            ),
            device = Device(
                platform = "/mobile/android"
            )
        )
        assertSame(expected.path, result.path)
        assertSame(expected.type, result.type)
        assertSame(expected.eventData, result.eventData)
        assertSame(expected.application.version, result.application.version)
        assertSame(expected.application.siteId, result.application.siteId)
    }
}
