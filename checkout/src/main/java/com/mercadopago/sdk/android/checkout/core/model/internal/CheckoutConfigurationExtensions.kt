package com.mercadopago.sdk.android.checkout.core.model.internal

import com.mercadopago.sdk.android.checkout.core.model.CheckoutType

internal const val CARD_FORM = "card_form"
internal const val AMOUNT_DEFAULT = "0"

internal fun CheckoutConfiguration.getAmount() =
    when (val type = checkoutType) {
        is CheckoutType.CardForm -> type.cardFormConfiguration.amount
    }

internal fun CheckoutConfiguration.getAmountOrZero(): String =
    getAmount()?.toPlainString()
        ?: AMOUNT_DEFAULT

internal fun CheckoutConfiguration?.toCheckoutType(): String =
    if (this?.checkoutType is CheckoutType.CardForm) {
        CARD_FORM
    } else {
        ""
    }
