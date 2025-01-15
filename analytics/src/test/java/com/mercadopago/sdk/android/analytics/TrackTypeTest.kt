package com.mercadopago.sdk.android.analytics

import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import org.junit.Assert.assertEquals
import org.junit.Test

internal class TrackTypeTest {

    @Test
    fun `trackTypeEnum should have two types`() {
        assertEquals(TrackType.VIEW.name, "VIEW")
        assertEquals(TrackType.EVENT.name, "EVENT")

        val expectedSize = 2
        val actualSize = TrackType.entries.size
        assertEquals(expectedSize, actualSize)
    }

    @Test
    fun `trackTypeEnum should return the right ordinal position`() {
        assertEquals(0, TrackType.VIEW.ordinal)
        assertEquals(1, TrackType.EVENT.ordinal)
    }

    @Test
    fun `trackTypeEnum should return the right item value name`() {
        assertEquals(TrackType.VIEW, TrackType.valueOf("VIEW"))
        assertEquals(TrackType.EVENT, TrackType.valueOf("EVENT"))
    }
}
