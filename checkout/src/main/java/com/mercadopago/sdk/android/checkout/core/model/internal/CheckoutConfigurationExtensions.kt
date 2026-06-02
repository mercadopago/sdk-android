package com.mercadopago.sdk.android.checkout.core.model.internal

import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType

internal const val CARD_TRANSACTION = "card_transaction"
internal const val CARD_SAVE = "card_save"
internal const val AMOUNT_DEFAULT = "0"

internal fun CheckoutConfiguration.getCardFormAmount() =
    (checkoutType as? MPCheckoutType.CardTransaction)
        ?.order
        ?.amount

internal fun CheckoutConfiguration.getCardFormAmountOrZero(): String =
    getCardFormAmount()?.toPlainString()
        ?: AMOUNT_DEFAULT

internal fun CheckoutConfiguration?.toCheckoutType(): String =
    when (this?.checkoutType) {
        is MPCheckoutType.CardSave -> CARD_SAVE
        is MPCheckoutType.CardTransaction -> CARD_TRANSACTION
        null -> ""
    }

internal fun CheckoutConfiguration?.getOrderId(): String = this.asCardTransaction()?.order?.orderId.orEmpty()

internal fun CheckoutConfiguration?.asCardTransaction(): MPCheckoutType.CardTransaction? =
    this?.checkoutType as? MPCheckoutType.CardTransaction
