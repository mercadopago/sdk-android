package com.mercadopago.sdk.android.checkout.domain.model.params

internal data class ProcessOrderParams(
    val orderId: String,
    val clientToken: String,
    val amount: String,
    val paymentMethodId: String,
    val paymentMethodType: String,
    val token: String,
    val installments: Int,
)
