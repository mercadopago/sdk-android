package com.mercadopago.sdk.android.checkout.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * CheckoutType enum, used to configure the checkout type
 */
sealed class CheckoutType : Parcelable {
    /**
     * CardSave class, used to configure the card save
     * @param cardFormConfiguration CardFormConfiguration
     */
    @Parcelize
    data class CardSave(val cardFormConfiguration: CardFormConfiguration = CardFormConfiguration()) : CheckoutType()
}
