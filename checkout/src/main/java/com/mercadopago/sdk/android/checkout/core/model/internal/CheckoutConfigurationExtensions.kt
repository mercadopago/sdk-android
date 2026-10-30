package com.mercadopago.sdk.android.checkout.core.model.internal

import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPSellerInfo

internal const val CARD_TRANSACTION = "card_transaction"
internal const val CARD_SAVE = "card_save"
internal const val PAYMENT = "payment"

internal fun CheckoutConfiguration?.toCheckoutType(): String =
    when (this?.checkoutType) {
        is MPCheckoutType.CardSave -> CARD_SAVE
        is MPCheckoutType.CardTransaction -> CARD_TRANSACTION
        is MPCheckoutType.Payment -> PAYMENT
        null -> ""
    }

internal fun CheckoutConfiguration?.isCardTransaction(): Boolean = this?.checkoutType is MPCheckoutType.CardTransaction

internal fun CheckoutConfiguration?.requiresOrderProcessing(): Boolean =
    this?.checkoutType is MPCheckoutType.CardTransaction || this?.checkoutType is MPCheckoutType.Payment

internal fun CheckoutConfiguration?.startsWithPayment(): Boolean = this?.checkoutType is MPCheckoutType.Payment

internal fun CheckoutConfiguration?.getSellerInfo(): MPSellerInfo? =
    when (val checkoutType = this?.checkoutType) {
        is MPCheckoutType.Payment -> checkoutType.sellerInfo
        is MPCheckoutType.CardTransaction -> checkoutType.sellerInfo
        is MPCheckoutType.CardSave, null -> null
    }

internal fun CheckoutConfiguration?.hasReviewAndConfirm(): Boolean =
    this?.screenConfigs?.any { it is ScreenConfig.ReviewAndConfirm } == true

internal fun CheckoutConfiguration?.hasStatusScreen(): Boolean {
    return this?.screenConfigs?.any { it is ScreenConfig.StatusScreen } == true
}

internal fun CheckoutConfiguration.buildScreensParam(): String? =
    screenConfigs.map { it.toScreen().name }.takeIf { it.isNotEmpty() }?.joinToString(",")
