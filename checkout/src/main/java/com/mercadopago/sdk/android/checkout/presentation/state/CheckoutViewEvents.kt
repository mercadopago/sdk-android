package com.mercadopago.sdk.android.checkout.presentation.state

import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError

internal sealed interface PaymentBrickViewEvent {
    data class OnOptionSelected(val optionId: String) : PaymentBrickViewEvent

    data class OnSecurityCodeRequired(val config: SecurityCodeScreenConfig) : PaymentBrickViewEvent

    data class OnFailure(val error: MercadoPagoCheckoutError) : PaymentBrickViewEvent

    data class OnUserCancelled(val context: MPUserCancelledContext.Payment) : PaymentBrickViewEvent
}

internal sealed interface CardPaymentViewEvent {
    data class OnSuccess(
        val payment: MPPaymentData,
        val installment: MPInstallmentData,
    ) : CardPaymentViewEvent

    data class OnFailure(val error: MercadoPagoCheckoutError) : CardPaymentViewEvent

    data class OnUserCancelled(val context: MPUserCancelledContext) : CardPaymentViewEvent

    data class OnBackPressed(val context: MPUserCancelledContext) : CardPaymentViewEvent
}

internal sealed interface InstallmentViewEvent {
    data class OnSuccess(val installment: Int) : InstallmentViewEvent

    data class OnFailure(val error: MercadoPagoCheckoutError) : InstallmentViewEvent

    data class OnUserCancelled(val context: MPUserCancelledContext) : InstallmentViewEvent
}
