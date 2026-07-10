package com.mercadopago.sdk.android.checkout.domain.model

import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsDisplayType

internal data class MPInstallmentData(
    val quotas: List<Quota> = emptyList(),
    val display: InstallmentDisplay = InstallmentDisplay(),
    val selectedInstallment: Int? = null,
) {
    internal data class InstallmentDisplay(
        val title: String = "",
        val currencySymbol: String = "",
        val displayType: InstallmentsDisplayType = InstallmentsDisplayType.RadioButton,
        val footer: InstallmentFooterDisplay = InstallmentFooterDisplay(),
    )

    internal data class InstallmentFooterDisplay(
        val footerTitle: String = "",
        val lastFourDigits: String = "",
        val brand: String = "",
        val buttonLabel: String = "",
    )
}
