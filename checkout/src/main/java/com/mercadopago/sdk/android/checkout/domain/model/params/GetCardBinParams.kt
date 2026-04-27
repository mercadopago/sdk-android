package com.mercadopago.sdk.android.checkout.domain.model.params

import com.mercadopago.sdk.android.checkout.domain.usecase.CardBinFilter

internal data class GetCardBinParams(
    val bin: String,
    val amount: String,
    val checkoutType: String,
    val processingMode: String,
    val filter: CardBinFilter,
)
