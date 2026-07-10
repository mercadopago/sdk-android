package com.mercadopago.sdk.android.checkout.core.model

import android.os.Parcelable
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutTypeConfiguration
import kotlinx.parcelize.Parcelize

/**
 * MPOrder class, used to configure the card form
 * @param orderId orderId
 * @param clientToken client token
 */
@Parcelize
data class MPOrder(
    val orderId: String,
    val clientToken: String,
) : CheckoutTypeConfiguration, Parcelable
