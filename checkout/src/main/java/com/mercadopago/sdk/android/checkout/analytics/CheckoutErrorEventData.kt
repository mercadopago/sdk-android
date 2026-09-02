package com.mercadopago.sdk.android.checkout.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.models.EventData

internal data class CheckoutErrorEventData(
    @SerializedName("error_type") val errorType: String,
    @SerializedName("observability_event_id") val observabilityEventId: String,
) : EventData
