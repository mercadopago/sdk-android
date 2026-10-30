package com.mercadopago.sdk.android.checkout.domain.model

internal data class ReviewConfirmViewData(
    val header: ReviewConfirmHeader,
    val items: List<ReviewConfirmItem>,
    val footerSummary: ReviewConfirmFooterSummary?,
    val footer: ReviewConfirmFooter,
)
