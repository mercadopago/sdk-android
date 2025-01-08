package com.mercadopago.sdk.android.analytics

import com.mercadopago.sdk.android.analytics.data.AnalyticsImpl
import com.mercadopago.sdk.android.analytics.data.remote.models.ApplicationRequest
import com.mercadopago.sdk.android.analytics.data.remote.models.DeviceRequest
import com.mercadopago.sdk.android.analytics.data.remote.models.TrackRequest
import com.mercadopago.sdk.android.analytics.data.remote.models.UserRequest
import com.mercadopago.sdk.android.analytics.domain.models.AnalyticsEventData
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import junit.framework.TestCase.assertSame
import org.junit.Test

class AnalyticsTrackerTest {

    @Test
    fun `trackEvent should set TrackType to event and return self`() {
        val analytics = AnalyticsImpl()
        val result = analytics.trackEvent("payment/credit_card")
        assertSame(analytics, result)
    }

    @Test
    fun `trackView should set TrackType to view and return self`() {
        val analytics = AnalyticsImpl()
        val result = analytics.trackView("checkout/review")
        assertSame(analytics, result)
    }

    @Test
    fun `track should set Track values and return Track`() {
        val eventData = AnalyticsEventData()
        val analytics = AnalyticsImpl()
            .setVersion("1.0.0")
            .trackView("payment/credit_card")
            .setEventData(eventData)
            .setSiteId("MLB")

        val result = analytics.send()
        val expected = TrackRequest(
            path = "payment/credit_card",
            user = UserRequest(
                uid = ""
            ),
            type = TrackType.VIEW.name,
            id = "",
            userTime = System.currentTimeMillis().toString(),
            eventData = eventData,
            application = ApplicationRequest(
                business = "mercadopago",
                siteId = "MLB",
                version = "1.0.0"
            ),
            device = DeviceRequest(
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
