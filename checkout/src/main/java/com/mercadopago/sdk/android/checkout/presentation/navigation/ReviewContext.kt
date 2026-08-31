package com.mercadopago.sdk.android.checkout.presentation.navigation

import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams

internal class ReviewContext(
    val params: ProcessOrderParams,
    val origin: ReviewOrigin,
) {
    override fun toString(): String = "ReviewContext(origin=$origin)"
}
