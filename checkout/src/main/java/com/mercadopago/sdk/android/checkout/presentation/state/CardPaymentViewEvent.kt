package com.mercadopago.sdk.android.checkout.presentation.state

internal sealed interface CardPaymentViewEvent {
    data object OnBackPressed : CardPaymentViewEvent
}
