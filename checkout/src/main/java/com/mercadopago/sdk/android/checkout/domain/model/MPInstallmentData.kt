package com.mercadopago.sdk.android.checkout.domain.model

import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsDisplayType
import java.math.BigDecimal

internal data class MPInstallmentData(
    // tech-debt: campo será removido na PR 4/4 do split 3917 quando CheckoutController migrar para usar quotas
    val showInstallment: Boolean = false,
    val brand: String = "",
    val lastFourDigits: String = "",
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
