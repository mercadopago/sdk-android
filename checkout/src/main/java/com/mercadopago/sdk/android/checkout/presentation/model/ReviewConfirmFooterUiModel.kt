package com.mercadopago.sdk.android.checkout.presentation.model

internal data class ReviewConfirmFooterUiModel(
    val buttonLabel: String,
    val totalAmount: String,
    val description: String?,
    val interestLabel: String?,
)
