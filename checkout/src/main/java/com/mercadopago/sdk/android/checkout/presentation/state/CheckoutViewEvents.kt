package com.mercadopago.sdk.android.checkout.presentation.state

import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError

/** One-shot view events emitted by CardPaymentViewModel. */
internal sealed interface CardPaymentViewEvent {
    /** Payment completed successfully with [payment] and [installment] data. */
    data class OnSuccess(
        val payment: MPPaymentData,
        val installment: MPInstallmentData,
    ) : CardPaymentViewEvent

    /** Payment failed with [error] details. */
    data class OnFailure(val error: MercadoPagoCheckoutError) : CardPaymentViewEvent

    /** User cancelled the flow; [context] describes which field was active. */
    data class OnUserCancelled(val context: MPUserCancelledContext) : CardPaymentViewEvent

    /** User pressed back; [context] describes which field was active. */
    data class OnBackPressed(val context: MPUserCancelledContext) : CardPaymentViewEvent
}

/** One-shot view events emitted by InstallmentsViewModel. */
internal sealed interface InstallmentViewEvent {
    /** Installment plan confirmed with the selected [installment] index. */
    data class OnSuccess(val installment: Int) : InstallmentViewEvent

    /** Installment fetch or selection failed with [error]. */
    data class OnFailure(val error: MercadoPagoCheckoutError) : InstallmentViewEvent

    /** User cancelled the installment selection; [context] describes where. */
    data class OnUserCancelled(val context: MPUserCancelledContext) : InstallmentViewEvent
}
