package com.mercadopago.sdk.android.checkout.domain.model

import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsDisplayType
import java.math.BigDecimal

internal data class MPInstallmentData(
    val brand: String,
    val lastFourDigits: String,
    val paymentMethodId: String,
    val paymentTypeId: String,
    val issuerId: String? = null,
    val transactionAmount: BigDecimal? = null,
    val quotas: List<Quota> = emptyList(),
    val display: Display = Display(),
    val selectedInstallment: Int? = null,
) {
    internal data class Display(
        val displayType: InstallmentsDisplayType = InstallmentsDisplayType.RadioButton,
        val headerChevron: String = "",
        val headerRadio: String = "",
        val interestFreeLabel: String = "",
        val totalLabel: String = "",
        val payButtonLabel: String = "",
    )
}
