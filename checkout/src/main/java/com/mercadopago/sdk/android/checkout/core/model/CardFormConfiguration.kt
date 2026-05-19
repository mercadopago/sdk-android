package com.mercadopago.sdk.android.checkout.core.model

import android.os.Parcelable
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutTypeConfiguration
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

/**
 * CardFormConfiguration class, used to configure the card form
 * @param amount BigDecimal
 * @param payer Payer
 * @param orderId Optional order identifier. When provided, the checkout will process the order
 * after tokenization and return the order result instead of the card token.
 */
@Parcelize
data class CardFormConfiguration(
    val amount: BigDecimal? = null,
    val payer: Payer? = null,
    val orderId: String? = null,
) : CheckoutTypeConfiguration, Parcelable {
    constructor(
        orderId: String,
        amount: BigDecimal,
        payer: Payer?,
    ) : this(amount, payer, orderId)
}
