package com.mercadopago.sdk.android.checkout.domain.model

import com.google.gson.annotations.SerializedName

internal data class ReviewConfirmItem(
    @SerializedName("type") val type: String,
    @SerializedName("label") val label: String,
    @SerializedName("value") val value: String?,
    @SerializedName("change_label") val changeLabel: String?,
)
