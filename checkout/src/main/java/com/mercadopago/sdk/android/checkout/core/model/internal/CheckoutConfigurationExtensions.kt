package com.mercadopago.sdk.android.checkout.core.model.internal

import com.mercadopago.sdk.android.checkout.core.model.CheckoutType

internal const val CARD_SAVE = "card_save"
internal const val AMOUNT_DEFAULT = "0"

@Suppress("FunctionOnlyReturningConstant")
internal fun CheckoutConfiguration.getCardFormAmount(): java.math.BigDecimal? = null

internal fun CheckoutConfiguration.getCardFormAmountOrZero(): String =
    getCardFormAmount()?.toPlainString()
        ?: AMOUNT_DEFAULT

internal fun CheckoutConfiguration?.toCheckoutType(): String =
    if (this?.checkoutType is CheckoutType.CardSave) {
        CARD_SAVE
    } else {
        ""
    }
