package com.mercadopago.sdk.android.checkout.core.model.internal

import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPOrder
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError

internal const val CARD_TRANSACTION = "card_transaction"
internal const val CARD_SAVE = "card_save"
internal const val PAYMENT = "payment"
private const val UNSUPPORTED = "Unsupported checkout type"

internal fun CheckoutConfiguration?.toCheckoutType(): String =
    when (this?.checkoutType) {
        is MPCheckoutType.CardSave -> CARD_SAVE
        is MPCheckoutType.CardTransaction -> CARD_TRANSACTION
        is MPCheckoutType.Payment -> PAYMENT
        null -> ""
    }

internal fun CheckoutConfiguration?.getOrderId(): String = this.asCardTransaction()?.order?.orderId.orEmpty()

internal fun CheckoutConfiguration?.getOrder(): MPOrder? = this.asCardTransaction()?.order

internal fun CheckoutConfiguration?.asCardTransaction(): MPCheckoutType.CardTransaction? =
    this?.checkoutType as? MPCheckoutType.CardTransaction

internal fun CheckoutConfiguration?.isCardTransaction(): Boolean = this?.checkoutType is MPCheckoutType.CardTransaction

internal fun CheckoutConfiguration?.startsWithPayment(): Boolean = this?.checkoutType is MPCheckoutType.Payment

internal fun CheckoutConfiguration?.hasReviewAndConfirm(): Boolean =
    this?.screenConfigs?.any { it is ScreenConfig.ReviewAndConfirm } == true

internal fun CheckoutConfiguration.buildScreensParam(): String? {
    val hasReviewAndConfirm = screenConfigs.any { it is ScreenConfig.ReviewAndConfirm }
    return if (hasReviewAndConfirm) "review_and_confirm" else null
}

internal fun MPCheckoutType<*, *>?.unsupportedTypeError(
    localized: ErrorLocalized,
): MercadoPagoCheckoutError.ConfigurationError =
    MercadoPagoCheckoutError.ConfigurationError(
        code = ErrorCode.INTEGRATION_ERROR,
        messageError = "$UNSUPPORTED: $this",
        localized = localized.name,
    )
