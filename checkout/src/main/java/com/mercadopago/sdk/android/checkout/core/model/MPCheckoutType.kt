package com.mercadopago.sdk.android.checkout.core.model

import android.os.Parcelable
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import kotlinx.parcelize.Parcelize

/**
 * MPCheckoutType enum, used to configure the checkout type.
 * The type parameter [T] determines the [com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData]
 * subtype returned in [com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult.Success].
 */
sealed class MPCheckoutType<out T : MPPaymentData> : Parcelable {
    /**
     * CardSave class, used to configure the card form.
     * On success, delivers [MPPaymentData.CardSave].
     */
    @Parcelize
    object CardSave : MPCheckoutType<MPPaymentData.CardSave>()

    /**
     * CardTransaction class, used to configure the card transaction.
     * On success, delivers [MPPaymentData.CardTransaction].
     * @param order MPOrder
     */
    @Parcelize
    data class CardTransaction(
        val order: MPOrder,
    ) : MPCheckoutType<MPPaymentData.CardTransaction>()

    /**
     * PaymentSelection class, used to configure the payment-method selection flow.
     * On success, delivers [MPPaymentData.CardTransaction].
     * @param order MPOrder
     * @param cardIds Optional saved-card ids used by the BFF to fetch and display the buyer's cards.
     * @param showReviewConfirm Whether to show the review & confirm screen before finishing. Default true.
     * @param showStatusScreen Whether to show the status screen at the end of the flow. Default true.
     */
    @Parcelize
    data class PaymentSelection(
        val order: MPOrder,
        val cardIds: List<String>?,
        val showReviewConfirm: Boolean = true,
        val showStatusScreen: Boolean = true,
    ) : MPCheckoutType<MPPaymentData.CardTransaction>()
}
