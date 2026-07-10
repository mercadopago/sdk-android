package com.mercadopago.sdk.android.checkout.domain.model.params

internal data class FetchPaymentBrickInitializationParams(
    val orderId: String,
    val totalAmount: String,
    val customerId: String? = null,
    val cardIds: String? = null,
)
