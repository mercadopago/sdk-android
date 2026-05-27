package com.mercadopago.sdk.android.checkout.presentation.state

internal data class AmountParts(
    val currencySymbol: String,
    val integerPart: String,
    val decimalPart: String,
)
