package com.mercadopago.sdk.android.checkout.domain.model.params

internal data class InitializeCardFormParams(
    val orderId: String?,
    val clientToken: String?,
    val checkoutType: String,
)
