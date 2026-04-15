package com.mercadopago.sdk.android.checkout.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Payer class, used to configure the payer
 * @param email String
 */
@Parcelize
data class Payer(
    val email: String,
) : Parcelable
