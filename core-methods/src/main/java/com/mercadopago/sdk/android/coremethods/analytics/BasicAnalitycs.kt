package com.mercadopago.sdk.android.coremethods.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.models.EventData

internal const val UNKNOWN_ERROR = "UNKNOWN_ERROR"

internal data class MetricErrorData(
    @SerializedName("error-code")
    val code: String = UNKNOWN_ERROR,
    @SerializedName("error-message")
    val message: String = UNKNOWN_ERROR,
) : EventData
