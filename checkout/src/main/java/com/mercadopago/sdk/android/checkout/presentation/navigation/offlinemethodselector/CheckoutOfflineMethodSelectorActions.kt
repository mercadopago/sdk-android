package com.mercadopago.sdk.android.checkout.presentation.navigation.offlinemethodselector

import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams

internal class CheckoutOfflineMethodSelectorActions(
    val onOpenReview: (ProcessOrderParams) -> Unit,
    val onProcessOrder: (ProcessOrderParams) -> Unit,
    val onBackClick: () -> Unit,
)
