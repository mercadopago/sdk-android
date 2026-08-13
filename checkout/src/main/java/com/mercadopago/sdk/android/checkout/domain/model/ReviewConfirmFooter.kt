package com.mercadopago.sdk.android.checkout.domain.model

import com.google.gson.annotations.SerializedName

internal data class ReviewConfirmFooter(
    @SerializedName("button") val button: ReviewConfirmButton,
    @SerializedName("total_amount") val totalAmount: String,
    @SerializedName("description") val description: String?,
    @SerializedName("interest_label") val interestLabel: String?,
)

internal data class ReviewConfirmButton(
    @SerializedName("label") val label: String,
)
