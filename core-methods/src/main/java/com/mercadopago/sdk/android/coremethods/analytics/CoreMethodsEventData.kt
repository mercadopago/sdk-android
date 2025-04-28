package com.mercadopago.sdk.android.coremethods.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.models.EventData

internal open class CoreMethodsEventData(
    @SerializedName("is_development")
    val isDevelopment: Boolean,
) : EventData
