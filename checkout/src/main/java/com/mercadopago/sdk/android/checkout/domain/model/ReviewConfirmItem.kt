package com.mercadopago.sdk.android.checkout.domain.model

import com.google.gson.annotations.SerializedName

internal data class ReviewConfirmItem(
    @SerializedName("type") val type: String,
    @SerializedName("label") val label: String,
    @SerializedName("value") val value: String?,
    @SerializedName("button") val button: ReviewConfirmItemButton?,
)

internal data class ReviewConfirmItemButton(
    @SerializedName("label") val label: String,
)
