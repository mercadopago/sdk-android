package com.mercadopago.sdk.android.checkout.core.model.internal

import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPOrder
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError

internal const val CARD_TRANSACTION = "card_transaction"
internal const val CARD_SAVE = "card_save"
private const val UNSUPPORTED = "Unsupported checkout type"

internal fun CheckoutConfiguration?.toCheckoutType(): String =
    when (this?.checkoutType) {
        is MPCheckoutType.CardSave -> CARD_SAVE
        is MPCheckoutType.CardTransaction -> CARD_TRANSACTION
        null -> ""
    }

internal fun CheckoutConfiguration?.getOrderId(): String = this.asCardTransaction()?.order?.orderId.orEmpty()

internal fun CheckoutConfiguration?.getOrder(): MPOrder? = this.asCardTransaction()?.order

internal fun CheckoutConfiguration?.asCardTransaction(): MPCheckoutType.CardTransaction? =
    this?.checkoutType as? MPCheckoutType.CardTransaction

internal fun CheckoutConfiguration?.isCardTransaction(): Boolean = this?.checkoutType is MPCheckoutType.CardTransaction

internal fun MPCheckoutType<*, *>?.unsupportedTypeError(
    localized: ErrorLocalized,
): MercadoPagoCheckoutError.ConfigurationError =
    MercadoPagoCheckoutError.ConfigurationError(
        code = ErrorCode.INTEGRATION_ERROR,
        messageError = "$UNSUPPORTED: $this",
        localized = localized.name,
    )
