package com.mercadopago.sdk.android.checkout.presentation.state

internal data class InstallmentsScreenState(
    val title: String? = null,
    val installmentsState: List<InstallmentState> = emptyList(),
    val footerState: FooterState? = null,
)

internal data class InstallmentState(
    val text: String,
    val description: String,
    val trailing: String,
    val interestFree: Boolean,
    val isSelected: Boolean,
    val number: Int,
)
