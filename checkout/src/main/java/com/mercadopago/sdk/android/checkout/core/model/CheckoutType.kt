package com.mercadopago.sdk.android.checkout.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * CheckoutType enum, used to configure the checkout type
 */
sealed class CheckoutType : Parcelable {
    /**
     * CardForm class, used to configure the card form
     * @param cardFormConfiguration CardFormConfiguration
     */
    @Parcelize
    data class CardForm(val cardFormConfiguration: CardFormConfiguration = CardFormConfiguration()) : CheckoutType()
}
