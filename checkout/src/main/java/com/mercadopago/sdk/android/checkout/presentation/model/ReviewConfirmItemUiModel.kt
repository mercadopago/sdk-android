package com.mercadopago.sdk.android.checkout.presentation.model

internal data class ReviewConfirmItemUiModel(
    val type: String,
    val label: String,
    val value: String?,
    val changeLabel: String?,
)
