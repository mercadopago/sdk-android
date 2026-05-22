package com.mercadopago.sdk.android.checkout.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * CheckoutType enum, used to configure the checkout type
 */
sealed class CheckoutType : Parcelable {
    /**
     * CardSave class, used to configure the card form
     */
    @Parcelize
    object CardSave : CheckoutType()

    /**
     * CardTransaction class, used to configure the card transaction
     * @param order Order
     */
    @Parcelize
    data class CardTransaction(
        val order: Order,
    ) : CheckoutType()
}
