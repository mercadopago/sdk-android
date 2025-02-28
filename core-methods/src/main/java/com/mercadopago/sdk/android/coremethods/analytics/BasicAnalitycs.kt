package com.mercadopago.sdk.android.coremethods.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.models.EventData

internal data class MetricErrorData(
    @SerializedName("error-code")
    val code: String,
    @SerializedName("error-message")
    val message: String
) : EventData
