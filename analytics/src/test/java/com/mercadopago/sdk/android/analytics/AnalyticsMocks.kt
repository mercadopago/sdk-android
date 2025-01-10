package com.mercadopago.sdk.android.analytics

import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType

val mockMetric = Metric(
    type = TrackType.EVENT,
    path = "metric/path",
    data = MockEventData(
        mockValue = ""
    )
)

data class MockEventData(
    val mockValue: String
) : EventData
