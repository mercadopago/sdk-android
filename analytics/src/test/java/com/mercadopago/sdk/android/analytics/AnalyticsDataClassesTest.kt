package com.mercadopago.sdk.android.analytics

import com.mercadopago.sdk.android.analytics.models.AnalyticsRequest
import com.mercadopago.sdk.android.analytics.models.Application
import com.mercadopago.sdk.android.analytics.models.Device
import com.mercadopago.sdk.android.analytics.models.Track
import com.mercadopago.sdk.android.analytics.models.User
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test

class AnalyticsDataClassesTest {

    @Test
    fun `test User data class`() {
        val user = User(uid = "user123")
        assertEquals("user123", user.uid)
    }

    @Test
    fun `test Application data class`() {
        val application = Application(business = "MyApp", siteId = "MLB", version = "1.0.0")
        assertEquals("MyApp", application.business)
        assertEquals("MLB", application.siteId)
        assertEquals("1.0.0", application.version)
    }

    @Test
    fun `test Device data class`() {
        val device = Device(platform = "Android")
        assertEquals("Android", device.platform)
    }

    @Test
    fun `test Track data class`() {
        val user = User(uid = "user123")
        val application = Application(business = "MyApp", siteId = "MLB", version = "1.0.0")
        val device = Device(platform = "Android")
        val eventData = AnalyticsEventData()
        val track = Track(
            path = "payment/credit_card",
            user = user,
            type = "EVENT",
            id = "track123",
            userTime = "1234567890",
            eventData = eventData,
            application = application,
            device = device
        )

        assertEquals("payment/credit_card", track.path)
        assertEquals("EVENT", track.type)
        assertEquals("track123", track.id)
        assertEquals(user, track.user)
        assertEquals(application, track.application)
        assertEquals(device, track.device)
        assertNotNull(track.eventData)
    }

    @Test
    fun `test AnalyticsRequest data class`() {
        val user = User(uid = "user123")
        val application = Application(business = "MyApp", siteId = "MLB", version = "1.0.0")
        val device = Device(platform = "Android")
        val eventData = AnalyticsEventData()
        val track = Track(
            path = "payment/credit_card",
            user = user,
            type = "EVENT",
            id = "track123",
            userTime = "1234567890",
            eventData = eventData,
            application = application,
            device = device
        )

        val analyticsRequest = AnalyticsRequest(tracks = listOf(track))
        assertEquals(1, analyticsRequest.tracks.size)
        assertEquals(track, analyticsRequest.tracks[0])
    }
}
