package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.checkout.domain.model.Payer

internal data class PendingOrderData(
    val token: String,
    val cardId: String?,
    val payer: Payer,
)
