package com.mercadopago.sdk.android.checkout.domain.model

internal data class MethodSelectionScreenData(
    val headerTitle: String,
    val selectionType: MethodSelectionLayoutType,
    val footer: MethodSelectionScreenFooter,
    val options: List<MethodSelectionOption>,
)

internal data class MethodSelectionScreenFooter(
    val totalLabel: String,
    val totalAmount: String,
    val button: MethodSelectionScreenButton? = null,
)

internal data class MethodSelectionScreenButton(
    val label: String,
)

internal data class MethodSelectionOption(
    val id: String,
    val name: String,
    val subtitle: String? = null,
    val iconUrl: String? = null,
)
