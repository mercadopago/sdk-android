package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.core.model.CardBrand
import com.mercadopago.sdk.android.checkout.core.model.CardType

internal data class CardBinFilter(
    val excludedPaymentTypes: List<CardType>,
    val excludedPaymentMethods: List<CardBrand>,
)
