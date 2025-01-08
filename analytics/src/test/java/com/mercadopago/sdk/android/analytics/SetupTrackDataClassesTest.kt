package com.mercadopago.sdk.android.analytics

import com.mercadopago.sdk.android.analytics.data.remote.models.AnalyticsRequest
import com.mercadopago.sdk.android.analytics.data.remote.models.ApplicationRequest
import com.mercadopago.sdk.android.analytics.data.remote.models.DeviceRequest
import com.mercadopago.sdk.android.analytics.data.remote.models.TrackRequest
import com.mercadopago.sdk.android.analytics.data.remote.models.UserRequest
import com.mercadopago.sdk.android.analytics.domain.models.AnalyticsEventData
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test

internal class SetupTrackDataClassesTest {

    @Test
    fun `test User data class`() {
        val user = UserRequest(uid = "user123")
        assertEquals("user123", user.uid)
    }

    @Test
    fun `test Application data class`() {
        val application = ApplicationRequest(business = "MyApp", siteId = "MLB", version = "1.0.0")
        assertEquals("MyApp", application.business)
        assertEquals("MLB", application.siteId)
        assertEquals("1.0.0", application.version)
    }

    @Test
    fun `test Device data class`() {
        val device = DeviceRequest(platform = "Android")
        assertEquals("Android", device.platform)
    }

    @Test
    fun `test Track data class`() {
        val user = UserRequest(uid = "user123")
        val application = ApplicationRequest(business = "MyApp", siteId = "MLB", version = "1.0.0")
        val device = DeviceRequest(platform = "Android")
        val eventData = AnalyticsEventData()
        val track = TrackRequest(
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
        val user = UserRequest(uid = "user123")
        val application = ApplicationRequest(business = "MyApp", siteId = "MLB", version = "1.0.0")
        val device = DeviceRequest(platform = "Android")
        val eventData = AnalyticsEventData()
        val track = TrackRequest(
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
