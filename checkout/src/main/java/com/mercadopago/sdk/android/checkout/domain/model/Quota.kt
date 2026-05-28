package com.mercadopago.sdk.android.checkout.domain.model

import java.math.BigDecimal

internal data class Quota(
    val installments: Int? = null,
    val installmentAmount: BigDecimal? = null,
    val totalAmount: BigDecimal? = null,
    val primaryLabel: String? = null,
    val secondaryLabel: String? = null,
    val tertiaryLabel: String? = null,
    val state: QuotaState = QuotaState.None,
    val accessibilityLabel: String? = null,
)

internal enum class QuotaState {
    None,
    Success,
}
