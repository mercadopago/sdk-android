package com.mercadopago.sdk.android.checkout.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.models.EventData

internal data class StatusScreenLoadEventData(
    @SerializedName("outcome")
    val outcome: String,
    @SerializedName("status_type")
    val statusType: String?,
) : EventData
