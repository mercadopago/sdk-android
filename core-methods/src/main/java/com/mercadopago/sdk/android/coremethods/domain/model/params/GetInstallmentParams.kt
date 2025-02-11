package com.mercadopago.sdk.android.coremethods.domain.model.params

internal data class GetInstallmentParams(
    val productId: String? = null,
    val bin: Int? = null,
    val processingMode: String? = null,
    val amount: Long? = null,
)
