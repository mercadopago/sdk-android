package com.mercadopago.sdk.android.checkout.domain.model

import com.google.gson.annotations.SerializedName

internal data class ReviewConfirmResponse(
    @SerializedName("header") val header: ReviewConfirmHeader,
    @SerializedName("items") val items: List<ReviewConfirmItem>,
    @SerializedName("footer_summary") val footerSummary: ReviewConfirmFooterSummary?,
    @SerializedName("footer") val footer: ReviewConfirmFooter,
)
