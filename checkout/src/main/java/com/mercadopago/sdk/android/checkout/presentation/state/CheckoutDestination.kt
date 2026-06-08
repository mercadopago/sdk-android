package com.mercadopago.sdk.android.checkout.presentation.state

import kotlinx.serialization.Serializable

@Serializable
internal sealed interface CheckoutDestination {
    @Serializable
    data object Loading : CheckoutDestination

    @Serializable
    data object Form : CheckoutDestination

    @Serializable
    data object Installment : CheckoutDestination

    @Serializable
    data object PaymentBrick : CheckoutDestination
}
