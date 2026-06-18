package com.mercadopago.sdk.android.checkout.domain.model.params

internal data class FetchPaymentBrickCardParams(
    val orderId: String,
    val bin: String,
)
