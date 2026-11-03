package com.mercadopago.sdk.android.checkout.presentation.model

internal data class ReviewConfirmFooterSummaryUiModel(
    val products: List<FooterSummaryRowUiModel>?,
    val coupon: FooterSummaryRowUiModel?,
    val interest: FooterSummaryInterestUiModel?,
)

internal data class FooterSummaryRowUiModel(
    val label: String,
    val amount: String,
)

internal data class FooterSummaryInterestUiModel(
    val title: String,
    val tooltipMessage: String,
    val amount: String,
)
