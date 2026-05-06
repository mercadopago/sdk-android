package com.mercadopago.sdk.android.checkout.presentation.state

import com.mercadopago.sdk.android.coremethods.domain.model.PayerCost

internal sealed interface CardPaymentViewEvent {
    data object OnBackPressed : CardPaymentViewEvent

    data class NavigateToInstallments(
        val payerCosts: List<PayerCost>,
        val lastFourDigits: String,
        val paymentMethodId: String,
    ) : CardPaymentViewEvent
}
