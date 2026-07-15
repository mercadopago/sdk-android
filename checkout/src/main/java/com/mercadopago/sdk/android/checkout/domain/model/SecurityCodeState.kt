package com.mercadopago.sdk.android.checkout.domain.model

internal data class SecurityCodeState(
    val label: String,
    val placeholder: String,
    val helper: String,
    val error: String,
    val length: Int,
    val maxLength: Int,
)
