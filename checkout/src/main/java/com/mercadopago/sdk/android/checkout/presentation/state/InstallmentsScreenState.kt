package com.mercadopago.sdk.android.checkout.presentation.state

internal data class InstallmentsScreenState(
    val title: String = "",
    val items: List<InstallmentState> = emptyList(),
    val footerState: FooterState = FooterState(),
    val displayType: InstallmentsDisplayType = InstallmentsDisplayType.RadioButton,
)

internal data class InstallmentState(
    val text: String,
    val trailing: String,
    val description: String,
    val isSelected: Boolean,
    val number: Int,
    val accessibilityLabel: String,
)
