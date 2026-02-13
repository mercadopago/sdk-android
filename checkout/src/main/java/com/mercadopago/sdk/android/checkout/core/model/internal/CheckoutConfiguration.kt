package com.mercadopago.sdk.android.checkout.core.model.internal

import com.mercadopago.sdk.android.checkout.core.model.CheckoutType
import com.mercadopago.sdk.android.checkout.core.model.PaymentMethod

/**
 * CheckoutConfiguration class, used to configure the checkout
 * @param checkoutType CheckoutType
 * @param paymentMethods List of payment methods
 */
internal data class CheckoutConfiguration(
    val checkoutType: CheckoutType,
    val paymentMethods: List<PaymentMethod>,
)
