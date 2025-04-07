package com.mercadopago.sdk.android.coremethods.domain.model.params

internal data class GetCardIssuersParams(
    val productId: String? = null,
    val bin: Int? = null,
    val paymentMethodId: String? = null,
)
