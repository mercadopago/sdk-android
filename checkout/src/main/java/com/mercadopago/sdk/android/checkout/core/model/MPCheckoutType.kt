package com.mercadopago.sdk.android.checkout.core.model

import android.os.Parcelable
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext
import kotlinx.parcelize.Parcelize

/**
 * MPCheckoutType sealed class, used to configure the checkout type.
 * The type parameter [T] determines the [com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData]
 * subtype returned in [com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult.Success].
 * The type parameter [C] determines the [com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext]
 * subtype returned in [com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult.UserCancelled].
 */
sealed class MPCheckoutType<out T : MPPaymentData, out C : MPUserCancelledContext> : Parcelable {
    /**
     * CardSave class, used to configure the card form.
     * On success, delivers [MPPaymentData.CardSave].
     * On cancellation, delivers [MPUserCancelledContext.CardSave].
     */
    @Parcelize
    object CardSave : MPCheckoutType<MPPaymentData.CardSave, MPUserCancelledContext.CardSave>()

    /**
     * CardTransaction class, used to configure the card transaction.
     * On success, delivers [MPPaymentData.CardTransaction].
     * On cancellation, delivers [MPUserCancelledContext.CardTransaction].
     * @param order MPOrder
     */
    @Parcelize
    data class CardTransaction(
        val order: MPOrder,
    ) : MPCheckoutType<MPPaymentData.CardTransaction, MPUserCancelledContext.CardTransaction>()

    /**
     * PaymentSelection class, used to configure the payment-method selection flow.
     * On success, delivers [MPPaymentData.Payment].
     * @param order MPOrder
     */
    @Parcelize
    data class Payment(
        val order: MPOrder,
    ) : MPCheckoutType<MPPaymentData.Payment, MPUserCancelledContext.Payment>()
}
