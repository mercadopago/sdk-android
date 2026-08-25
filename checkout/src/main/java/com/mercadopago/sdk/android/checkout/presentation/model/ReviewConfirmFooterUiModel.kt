package com.mercadopago.sdk.android.checkout.presentation.model

internal data class ReviewConfirmFooterUiModel(
    val buttonLabel: String,
    val currencySymbol: String?,
    val totalAmount: String,
    val totalLabel: String?,
    val installments: ReviewConfirmInstallmentsUiModel?,
    val description: String?,
    val interestLabel: String?,
)

internal data class ReviewConfirmInstallmentsUiModel(
    val label: String,
    val secondaryLabel: String?,
    val state: String?,
)
