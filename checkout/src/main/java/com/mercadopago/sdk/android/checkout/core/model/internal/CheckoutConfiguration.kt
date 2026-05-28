package com.mercadopago.sdk.android.checkout.core.model.internal

import android.os.Parcelable
import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPPaymentMethodConfig
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class CheckoutConfiguration(
    val checkoutType: MPCheckoutType<*>,
    val paymentMethodConfigs: List<MPPaymentMethodConfig>,
) : Parcelable
