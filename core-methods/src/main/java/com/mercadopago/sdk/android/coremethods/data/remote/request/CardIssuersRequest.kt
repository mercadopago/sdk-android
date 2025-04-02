package com.mercadopago.sdk.android.coremethods.data.remote.request

internal data class CardIssuersRequest(
    val productId: String? = null,
    val bin: Int? = null,
    val paymentMethodId: String? = null,
)
