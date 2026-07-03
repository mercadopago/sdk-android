package com.mercadopago.sdk.android.checkout.presentation.state

internal data class PaymentBrickScreenState(
    val title: String = "",
    val sections: List<PaymentSectionState> = emptyList(),
    val footerState: PaymentBrickFooterState? = null,
    val isLoading: Boolean = false,
)

internal data class PaymentSectionState(
    val title: String,
    val options: List<PaymentOptionState>,
)

internal data class PaymentOptionState(
    val id: String,
    val title: String,
    val thumbnailUrl: String? = null,
    val description: String? = null,
)

internal data class PaymentBrickFooterState(
    val totalLabel: String,
    val totalAmount: String,
)
