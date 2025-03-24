package com.mercadopago.sdk.android.coremethods.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.core.utils.KoverIgnore

internal const val UNKNOWN_ERROR = "UNKNOWN_ERROR"

@KoverIgnore("in development")
internal data class MetricErrorData(
    @SerializedName("error")
    val error: String = UNKNOWN_ERROR,
) : EventData
