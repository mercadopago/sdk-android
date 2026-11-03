package com.mercadopago.sdk.android.checkout.core.model.internal

import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPOrder

internal fun CheckoutConfiguration?.asCardTransaction(): MPCheckoutType.CardTransaction? =
    this?.checkoutType as? MPCheckoutType.CardTransaction

internal fun CheckoutConfiguration?.getOrderId(): String = this.asCardTransaction()?.order?.orderId.orEmpty()

internal fun CheckoutConfiguration?.getOrder(): MPOrder? = this.asCardTransaction()?.order

internal fun CheckoutConfiguration?.paymentBrickOrderId(): String? =
    when (val checkoutType = this?.checkoutType) {
        is MPCheckoutType.Payment -> checkoutType.order.orderId
        is MPCheckoutType.CardTransaction -> checkoutType.order.orderId
        is MPCheckoutType.CardSave, null -> null
    }

internal fun CheckoutConfiguration?.paymentBrickClientToken(): String? =
    when (val checkoutType = this?.checkoutType) {
        is MPCheckoutType.Payment -> checkoutType.order.clientToken
        is MPCheckoutType.CardTransaction -> checkoutType.order.clientToken
        is MPCheckoutType.CardSave, null -> null
    }
