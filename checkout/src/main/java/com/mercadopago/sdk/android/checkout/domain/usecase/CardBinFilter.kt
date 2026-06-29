package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.core.model.MPCardBrand
import com.mercadopago.sdk.android.checkout.core.model.MPCardType

internal data class CardBinFilter(
    val excludedPaymentTypes: List<MPCardType>,
    val excludedPaymentMethods: List<MPCardBrand>,
)
