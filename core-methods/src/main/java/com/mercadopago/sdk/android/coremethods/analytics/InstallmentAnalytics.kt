package com.mercadopago.sdk.android.coremethods.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType

internal fun provideMetricInstallmentFetch(isDeveloping: Boolean) = Metric(
    type = TrackType.EVENT,
    path = "/bricks-sdk-native/installments",
    data = DevelopmentData(isDeveloping = isDeveloping)
)

internal data class DevelopmentData(
    @SerializedName("is_development")
    val isDeveloping: Boolean
) : EventData
