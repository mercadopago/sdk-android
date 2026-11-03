package com.mercadopago.sdk.android.checkout.domain.model

import com.google.gson.annotations.SerializedName

internal data class ReviewConfirmFooterSummary(
    @SerializedName("products") val products: List<FooterSummaryRow>?,
    @SerializedName("coupon") val coupon: FooterSummaryRow?,
    @SerializedName("interest") val interest: FooterSummaryInterest?,
)

internal data class FooterSummaryRow(
    @SerializedName("label") val label: String,
    @SerializedName("amount") val amount: String,
)

internal data class FooterSummaryInterest(
    @SerializedName("title") val title: String,
    @SerializedName("tooltip_message") val tooltipMessage: String,
    @SerializedName("amount") val amount: String,
)
