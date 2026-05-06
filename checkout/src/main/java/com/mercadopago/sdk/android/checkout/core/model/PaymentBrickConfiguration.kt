package com.mercadopago.sdk.android.checkout.core.model

import android.os.Parcelable
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutTypeConfiguration
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

/**
 * PaymentBrickConfiguration class, used to configure the payment brick
 * @param amount BigDecimal
 */
@Parcelize
data class PaymentBrickConfiguration(
    val amount: BigDecimal? = null,
) : CheckoutTypeConfiguration, Parcelable
