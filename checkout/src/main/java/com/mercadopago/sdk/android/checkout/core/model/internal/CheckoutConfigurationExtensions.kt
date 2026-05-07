package com.mercadopago.sdk.android.checkout.core.model.internal

import com.mercadopago.sdk.android.checkout.core.model.CheckoutType

internal const val CARD_FORM = "card_form"
internal const val AMOUNT_DEFAULT = "0"

internal fun CheckoutConfiguration.getCardFormAmount() =
    (checkoutType as? CheckoutType.CardForm)
        ?.cardFormConfiguration
        ?.amount

internal fun CheckoutConfiguration.getCardFormAmountOrZero(): String =
    getCardFormAmount()?.toPlainString()
        ?: AMOUNT_DEFAULT

internal fun CheckoutConfiguration.getAmount() =
    when (val type = checkoutType) {
        is CheckoutType.CardForm -> type.cardFormConfiguration.amount
    }

internal fun CheckoutConfiguration?.toCheckoutType(): String =
    if (this?.checkoutType is CheckoutType.CardForm) {
        CARD_FORM
    } else {
        ""
    }
