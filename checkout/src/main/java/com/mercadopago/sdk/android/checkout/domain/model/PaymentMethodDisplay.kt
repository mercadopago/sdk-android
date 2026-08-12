package com.mercadopago.sdk.android.checkout.domain.model

internal data class PaymentMethodDisplay(
    val label: String,
    val value: String,
    val changeLabel: String?,
)
