package com.mercadopago.sdk.android.checkout.data.remote.request

internal data class OrderProcessRequest(
    val amount: String,
    val paymentMethodId: String,
    val paymentMethodType: String,
    val token: String,
    val installments: Int,
)
