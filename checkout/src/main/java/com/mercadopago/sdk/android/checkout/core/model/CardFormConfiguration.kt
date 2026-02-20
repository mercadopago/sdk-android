package com.mercadopago.sdk.android.checkout.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

/**
 * CardFormConfiguration class, used to configure the card form
 * @param amount BigDecimal
 * @param payer Payer
 */
@Parcelize
data class CardFormConfiguration(
    val amount: BigDecimal? = null,
    val payer: Payer? = null,
) : Parcelable
