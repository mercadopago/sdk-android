package com.mercadopago.sdk.android.checkout.domain.model

internal data class EmailFieldState(
    val label: String,
    val maskedEmail: String,
    val changeLabel: String?,
)
