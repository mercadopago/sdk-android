package com.mercadopago.sdk.android.checkout.core.model.internal

import android.os.Parcelable
import com.mercadopago.sdk.android.checkout.core.model.CheckoutType
import com.mercadopago.sdk.android.checkout.core.model.PaymentMethod
import kotlinx.parcelize.Parcelize

/**
 * Configuration class, used to configure the checkout
 * @param checkoutType CheckoutType
 * @param paymentMethods List of payment methods
 */
@Parcelize
internal data class Configuration(
    val checkoutType: CheckoutType,
    val paymentMethods: List<PaymentMethod>,
) : Parcelable
