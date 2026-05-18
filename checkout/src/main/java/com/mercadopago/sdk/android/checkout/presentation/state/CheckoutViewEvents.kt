package com.mercadopago.sdk.android.checkout.presentation.state

import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.UserCancelledContext

internal sealed interface CardPaymentViewEvent {
    data class OnSuccess(
        val payment: MPPaymentData,
        val installment: MPInstallmentData,
    ) : CardPaymentViewEvent

    data class OnFailure(val error: MercadoPagoCheckoutError) : CardPaymentViewEvent

    data class OnUserCancelled(val context: UserCancelledContext) : CardPaymentViewEvent
}

internal sealed interface InstallmentViewEvent {
    data class OnSuccess(
        val installment: Int = 0,
        // tech-debt: campo será removido na PR 4/4 quando CheckoutController migrar para installment: Int
        val payment: MPPaymentData = MPPaymentData(
            token = "",
            transactionAmount = null,
            paymentMethodId = "",
            paymentTypeId = "",
            payer = null,
            installment = null,
            issuerId = null,
        ),
    ) : InstallmentViewEvent

    data class OnFailure(val error: MercadoPagoCheckoutError) : InstallmentViewEvent

    data class OnUserCancelled(val context: UserCancelledContext) : InstallmentViewEvent
}
