package com.mercadopago.sdk.android.checkout.domain.model

internal data class MPInstallmentData(
    val quotas: List<Quota> = emptyList(),
    val display: InstallmentDisplay = InstallmentDisplay(),
    val selectedInstallment: Int? = null,
    val defaultInstallments: Int? = null,
) {
    fun defaultQuotaNumber(): Int? {
        return defaultInstallments?.takeIf { default -> quotas.any { it.installments == default } }
    }

    internal data class InstallmentDisplay(
        val title: String = "",
        val currencySymbol: String = "",
        val displayType: SelectionDisplayType = SelectionDisplayType.RadioButton,
        val footer: InstallmentFooterDisplay = InstallmentFooterDisplay(),
    )

    internal data class InstallmentFooterDisplay(
        val footerTitle: String = "",
        val lastFourDigits: String = "",
        val brand: String = "",
        val buttonLabel: String = "",
    )
}
