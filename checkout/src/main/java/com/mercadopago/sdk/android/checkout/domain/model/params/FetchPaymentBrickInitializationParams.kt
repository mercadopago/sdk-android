package com.mercadopago.sdk.android.checkout.domain.model.params

internal data class FetchPaymentBrickInitializationParams(
    val orderId: String,
    val clientToken: String,
)
