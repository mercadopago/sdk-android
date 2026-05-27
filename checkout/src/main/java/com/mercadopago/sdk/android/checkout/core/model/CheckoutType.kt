package com.mercadopago.sdk.android.checkout.core.model

import android.os.Parcelable
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import kotlinx.parcelize.Parcelize

/**
 * CheckoutType enum, used to configure the checkout type.
 * The type parameter [T] determines the [com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData]
 * subtype returned in [com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult.Success].
 */
sealed class CheckoutType<out T : MPPaymentData> : Parcelable {
    /**
     * CardSave class, used to configure the card form.
     * On success, delivers [MPPaymentData.CardSave].
     */
    @Parcelize
    object CardSave : CheckoutType<MPPaymentData.CardSave>()

    /**
     * CardTransaction class, used to configure the card transaction.
     * On success, delivers [MPPaymentData.CardTransaction].
     * @param order Order
     */
    @Parcelize
    data class CardTransaction(
        val order: Order,
    ) : CheckoutType<MPPaymentData.CardTransaction>()
}
