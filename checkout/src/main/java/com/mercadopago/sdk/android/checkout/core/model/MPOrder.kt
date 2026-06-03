package com.mercadopago.sdk.android.checkout.core.model

import android.os.Parcelable
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutTypeConfiguration
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

/**
 * MPOrder class, used to configure the card form
 * @param orderId orderId
 * @param amount BigDecimal
 * @param payer MPPayer
 */
@Parcelize
data class MPOrder(
    val orderId: String,
    val amount: BigDecimal,
    val payer: MPPayer,
) : CheckoutTypeConfiguration, Parcelable
