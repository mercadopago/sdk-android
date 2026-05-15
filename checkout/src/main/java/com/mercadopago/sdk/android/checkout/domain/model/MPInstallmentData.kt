package com.mercadopago.sdk.android.checkout.domain.model

import kotlinx.serialization.Serializable
import java.math.BigDecimal

internal data class MPInstallmentData(
    val brand: String,
    val lastFourDigits: String,
    val transactionAmount: BigDecimal? = null,
    val quotas: List<Quota> = emptyList(),
    val display: Display = Display(),
    val selectedInstallment: Int? = null,
) {
    internal data class Display(
        val displayType: InstallmentsDisplayType = InstallmentsDisplayType.RadioButton,
        val title: String = "",
        val totalLabel: String = "",
        val buttonLabel: String = "",
        val currencySymbol: String = "",
    )
}

internal data class Quota(
    val installments: Int? = null,
    val installmentAmount: BigDecimal? = null,
    val totalAmount: BigDecimal? = null,
    val primaryLabel: String? = null,
    val secondaryLabel: String? = null,
    val tertiaryLabel: String? = null,
    val state: QuotaState = QuotaState.None,
)

internal enum class QuotaState {
    None,
    Selected,
    Disabled,
}

@Serializable
internal enum class InstallmentsDisplayType {
    Chevron,
    RadioButton,
}
