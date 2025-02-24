package com.mercadopago.sdk.android.coremethods.data.remote.request

internal data class InstallmentsRequest(
    val productId: String? = null,
    val bin: Int? = null,
    val processingMode: String? = null,
    val amount: Long? = null,
)
