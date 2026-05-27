package com.mercadopago.sdk.android.checkout.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * MPCheckoutType enum, used to configure the checkout type
 */
sealed class MPCheckoutType : Parcelable {
    /**
     * CardSave class, used to configure the card form
     */
    @Parcelize
    object CardSave : MPCheckoutType()

    /**
     * CardTransaction class, used to configure the card transaction
     * @param cardFormConfiguration MPOrder
     */
    @Parcelize
    data class CardTransaction(
        val cardFormConfiguration: MPOrder = MPOrder(),
    ) : MPCheckoutType()
}
