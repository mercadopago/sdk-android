package com.mercadopago.sdk.android.analytics

import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType

internal fun mockMetric(
    path: String = "test/path",
    trackType: TrackType = TrackType.EVENT,
    data: EventData
): Metric {
    return Metric(
        path = path,
        type = trackType,
        data = data
    )
}

internal fun mockEventData(value: String = "data value"): EventData {
    return MockEventData(
        mockValue = value
    )
}

internal data class MockEventData(
    val mockValue: String
) : EventData
