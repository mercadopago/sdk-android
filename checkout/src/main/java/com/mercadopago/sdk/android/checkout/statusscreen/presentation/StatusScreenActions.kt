package com.mercadopago.sdk.android.checkout.statusscreen.presentation

internal class StatusScreenActions(
    val onBack: () -> Unit,
    val onOpenPdf: (String) -> Unit,
    val onChangePaymentMethod: () -> Unit,
    val onCopy: () -> Unit,
    val onOpenUrl: (String) -> Unit,
)
