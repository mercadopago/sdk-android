package com.mercadopago.sdk.android.checkout.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.models.EventData

internal data class ReviewConfirmPayerFieldChangedEventData(
    @SerializedName("changed_field")
    val changedField: String,
) : EventData
