package com.mercadopago.sdk.android.checkout.domain.model

import java.math.BigDecimal

internal data class Quota(
    val installments: Int? = null,
    val installmentAmount: BigDecimal? = null,
    val totalAmount: BigDecimal? = null,
)
