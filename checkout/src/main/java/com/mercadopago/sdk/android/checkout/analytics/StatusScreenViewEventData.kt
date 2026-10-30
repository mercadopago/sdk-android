package com.mercadopago.sdk.android.checkout.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.models.EventData

internal data class StatusScreenViewEventData(
    @SerializedName("status_type")
    val statusType: String,
    @SerializedName("can_retry")
    val canRetry: Boolean? = null,
) : EventData
