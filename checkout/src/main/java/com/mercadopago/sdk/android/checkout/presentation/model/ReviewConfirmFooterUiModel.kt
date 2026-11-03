package com.mercadopago.sdk.android.checkout.presentation.model

import java.math.BigDecimal

internal data class ReviewConfirmFooterUiModel(
    val buttonLabel: String,
    val currencySymbol: String?,
    val totalAmount: BigDecimal,
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
