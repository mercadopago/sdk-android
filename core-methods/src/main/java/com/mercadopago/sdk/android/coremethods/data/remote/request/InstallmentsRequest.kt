package com.mercadopago.sdk.android.coremethods.data.remote.request

import java.math.BigDecimal

internal data class InstallmentsRequest(
    val productId: String? = null,
    val bin: Int? = null,
    val processingMode: String? = null,
    val amount: BigDecimal? = null,
)
