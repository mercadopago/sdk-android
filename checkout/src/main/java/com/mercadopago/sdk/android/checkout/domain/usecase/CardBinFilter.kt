package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.core.model.MPCardBrand
import com.mercadopago.sdk.android.checkout.core.model.MPCardType

internal data class CardBinFilter(
    val cardTypes: List<MPCardType>,
    val cardBrands: List<MPCardBrand>,
)
