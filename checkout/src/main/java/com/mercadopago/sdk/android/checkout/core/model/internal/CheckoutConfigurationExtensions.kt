package com.mercadopago.sdk.android.checkout.core.model.internal

import com.mercadopago.sdk.android.checkout.core.model.CheckoutType

internal const val CARD_TRANSACTION = "card_transaction"
internal const val CARD_SAVE = "card_save"
internal const val AMOUNT_DEFAULT = "0"

internal fun CheckoutConfiguration.getCardFormAmount() =
    (checkoutType as? CheckoutType.CardTransaction)
        ?.cardFormConfiguration
        ?.amount

internal fun CheckoutConfiguration.getCardFormAmountOrZero(): String =
    getCardFormAmount()?.toPlainString()
        ?: AMOUNT_DEFAULT

internal fun CheckoutConfiguration?.toCheckoutType(): String =
    when (this?.checkoutType) {
        is CheckoutType.CardSave -> CARD_SAVE
        is CheckoutType.CardTransaction -> CARD_TRANSACTION
        null -> ""
    }
