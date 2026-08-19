package com.mercadopago.sdk.android.checkout.domain.model

internal data class MPInstallmentData(
    val quotas: List<Quota> = emptyList(),
    val display: InstallmentDisplay = InstallmentDisplay(),
    val selectedInstallment: Int? = null,
) {
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
