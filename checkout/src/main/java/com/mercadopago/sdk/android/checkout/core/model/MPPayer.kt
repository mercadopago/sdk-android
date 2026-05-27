package com.mercadopago.sdk.android.checkout.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * MPPayer class, used to configure the payer
 * @param email String
 */
@Parcelize
data class MPPayer(
    val email: String,
) : Parcelable
