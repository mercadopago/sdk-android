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
}
