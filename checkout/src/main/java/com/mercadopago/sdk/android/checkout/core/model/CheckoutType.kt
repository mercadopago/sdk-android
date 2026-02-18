package com.mercadopago.sdk.android.checkout.core.model

/**
 * CheckoutType enum, used to configure the checkout type
 */
sealed class CheckoutType {
    /**
     * CardForm class, used to configure the card form
     * @param cardFormConfiguration CardFormConfiguration
     */
    class CardForm(val cardFormConfiguration: CardFormConfiguration? = null) : CheckoutType()
}
