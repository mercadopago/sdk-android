package com.mercadopago.sdk.android.analytics.data.remote.models.response

import com.google.gson.annotations.SerializedName

internal data class ValidateMetricResponse(
    @SerializedName("passed")
    val passed: Boolean? = false,
)
