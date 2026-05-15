package com.mercadopago.sdk.android.checkout.presentation.state

import com.mercadopago.sdk.android.checkout.domain.model.InstallmentsDisplayType

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
)
