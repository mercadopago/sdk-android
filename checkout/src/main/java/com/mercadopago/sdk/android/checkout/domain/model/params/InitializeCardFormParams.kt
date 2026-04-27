package com.mercadopago.sdk.android.checkout.domain.model.params

internal data class InitializeCardFormParams(
    val amount: String,
    val checkoutType: String,
)
