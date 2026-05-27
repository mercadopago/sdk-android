package com.mercadopago.sdk.android.checkout.core.model.internal

import android.os.Parcelable
import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPPaymentMethod
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class CheckoutConfiguration(
    val checkoutType: MPCheckoutType<*>,
    val paymentMethods: List<MPPaymentMethod>,
) : Parcelable
