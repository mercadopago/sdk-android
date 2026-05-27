package com.mercadopago.sdk.android.checkout.core.model.internal

import android.os.Parcelable
import com.mercadopago.sdk.android.checkout.core.model.CheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPPaymentMethodConfig
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class CheckoutConfiguration(
    val checkoutType: CheckoutType,
    val paymentMethodConfigs: List<MPPaymentMethodConfig>,
) : Parcelable
