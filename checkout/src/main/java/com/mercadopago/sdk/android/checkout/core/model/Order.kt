package com.mercadopago.sdk.android.checkout.core.model

import android.os.Parcelable
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutTypeConfiguration
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

/**
 * Order class, used to configure the card form
 * @param amount BigDecimal
 * @param payer Payer
 */
@Parcelize
data class Order(
    val amount: BigDecimal? = null,
    val payer: Payer? = null,
) : CheckoutTypeConfiguration, Parcelable
