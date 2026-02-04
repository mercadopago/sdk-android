package com.mercadopago.sdk.android.checkout.presentation.state

internal data class FooterState(
    val title: String,
    val currencySymbol: String,
    val amountIntegerPart: String,
    val amountDecimalPart: String,
    val subtitle: String? = null
)
