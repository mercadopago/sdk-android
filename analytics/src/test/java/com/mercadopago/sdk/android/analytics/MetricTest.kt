package com.mercadopago.sdk.android.analytics

import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import junit.framework.TestCase.assertEquals
import org.junit.Test

internal class MetricTest {

    @Test
    fun `test metric properties with event track type`() {
        val path = "path"
        val trackType = TrackType.EVENT
        val data = mockEventData("teste")
        val metric = mockMetric(path, trackType, data)

        assertEquals(trackType, metric.type)
        assertEquals(path, metric.path)
        assertEquals(data, metric.data)
    }

    @Test
    fun `test metric properties with view track type`() {
        val path = "path"
        val trackType = TrackType.VIEW
        val data = mockEventData("teste")
        val metric = mockMetric(path, trackType, data)

        assertEquals(trackType, metric.type)
        assertEquals(path, metric.path)
        assertEquals(data, metric.data)
    }
}
