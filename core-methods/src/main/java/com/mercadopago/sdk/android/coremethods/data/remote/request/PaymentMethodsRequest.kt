package com.mercadopago.sdk.android.coremethods.data.remote.request

internal data class PaymentMethodsRequest(
    val productId: String? = null,
    val bin: Int? = null,
)
