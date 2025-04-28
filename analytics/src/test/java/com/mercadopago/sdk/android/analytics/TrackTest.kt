package com.mercadopago.sdk.android.analytics

import com.mercadopago.sdk.android.analytics.data.remote.models.request.AnalyticsRequest
import com.mercadopago.sdk.android.analytics.data.remote.models.request.ApplicationRequest
import com.mercadopago.sdk.android.analytics.data.remote.models.request.DeviceRequest
import com.mercadopago.sdk.android.analytics.data.remote.models.request.TrackRequest
import com.mercadopago.sdk.android.analytics.data.remote.models.request.UserRequest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test

internal class TrackTest {

    @Test
    fun `test User data class`() {
        val user = UserRequest(
            uid = "user123",
            sessionId = "sessionId"
        )
        assertEquals("user123", user.uid)
        assertEquals("sessionId", user.sessionId)
    }

    @Test
    fun `test Application data class`() {
        val application = ApplicationRequest(
            business = "MyApp",
            siteId = "MLB",
            version = "1.0.0",
            appName = "MercadoPago",
        )
        assertEquals("MyApp", application.business)
        assertEquals("MLB", application.siteId)
        assertEquals("1.0.0", application.version)
        assertEquals("MercadoPago", application.appName)
    }

    @Test
    fun `test Device data class`() {
        val device = DeviceRequest(
            platform = "Android",
            connectivityType = "wifi",
            osVersion = "31",
        )
        assertEquals("Android", device.platform)
        assertEquals("wifi", device.connectivityType)
        assertEquals("31", device.osVersion)
    }

    @Test
    fun `test Track data class`() {
        val user = UserRequest(
            uid = "user123",
            sessionId = "sessionId",
        )
        val application = ApplicationRequest(
            business = "MyApp",
            siteId = "MLB",
            version = "1.0.0",
            appName = "MercadoPago",
        )
        val device = DeviceRequest(
            platform = "Android",
            connectivityType = "wifi",
            osVersion = "31",
        )
        val eventData = MockEventData("")
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
        assertEquals(eventData, track.eventData)
        assertEquals(application, track.application)
        assertEquals(device, track.device)
        assertNotNull(track.eventData)
        assertEquals("MercadoPago", track.application.appName)
        assertEquals("wifi", track.device.connectivityType)
        assertEquals("31", track.device.osVersion)
    }

    @Test
    fun `test AnalyticsRequest data class`() {
        val user = UserRequest(
            uid = "user123",
            sessionId = "sessionId",
        )
        val application = ApplicationRequest(
            business = "MyApp",
            siteId = "MLB",
            version = "1.0.0",
            appName = "MercadoPago",
        )
        val device = DeviceRequest(
            platform = "Android",
            connectivityType = "wifi",
            osVersion = "31",
        )
        val eventData = MockEventData("")
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
        assertEquals("MercadoPago", track.application.appName)
        assertEquals("wifi", track.device.connectivityType)
        assertEquals("31", track.device.osVersion)
    }
}
