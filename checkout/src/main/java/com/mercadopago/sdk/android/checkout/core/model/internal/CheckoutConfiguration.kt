package com.mercadopago.sdk.android.checkout.core.model.internal

import android.os.Parcelable
import com.mercadopago.sdk.android.checkout.core.model.CheckoutType
import com.mercadopago.sdk.android.checkout.core.model.PaymentMethod
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class CheckoutConfiguration(
    val checkoutType: CheckoutType,
    val paymentMethods: List<PaymentMethod>,
) : Parcelable

internal fun CheckoutConfiguration.getCardFormAmount() =
    (checkoutType as? CheckoutType.CardForm)
        ?.cardFormConfiguration
        ?.amount
