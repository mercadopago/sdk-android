package com.mercadopago.sdk.android.checkout.core.model.internal

import android.os.Parcelable
import com.mercadopago.sdk.android.checkout.core.model.CheckoutType
import com.mercadopago.sdk.android.checkout.core.model.PaymentMethod
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class Configuration(
    val checkoutType: CheckoutType,
    val paymentMethods: List<PaymentMethod>,
) : Parcelable
