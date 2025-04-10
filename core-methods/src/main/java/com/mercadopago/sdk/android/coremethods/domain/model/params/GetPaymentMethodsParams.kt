package com.mercadopago.sdk.android.coremethods.domain.model.params

internal data class GetPaymentMethodsParams(
    val productId: String? = null,
    val bin: Int? = null,
)
