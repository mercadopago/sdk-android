package com.mercadopago.sdk.android.checkout.statusscreen.presentation

import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenViewData

internal sealed interface StatusScreenState {
    data object Idle : StatusScreenState

    data object Loading : StatusScreenState

    data class Ready(val data: StatusScreenViewData) : StatusScreenState

    data class Unavailable(
        val paymentProcessed: OrderProcessOutput.PaymentProcessed?,
        val lastFourDigits: String?,
        val orderStatus: String? = null,
    ) : StatusScreenState
}
