package com.mercadopago.sdk.android.coremethods.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.core.utils.isDebugApp

internal open class CoreMethodsEventData(
    @SerializedName("is_development")
    val isDevelopment: Boolean = isDebugApp(),
) : EventData
