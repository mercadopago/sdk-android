package com.mercadopago.sdk.android.analytics

import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MetricTest {

    @Test
    fun `test metric properties`() {
        val path = "path"
        val trackType = TrackType.EVENT
        val data = mockEventData("teste")
        val metric = mockMetric(path, trackType, data)

        assertEquals(trackType, metric.type)
        assertEquals(path, metric.path)
        assertEquals(data, metric.data)
    }
}
