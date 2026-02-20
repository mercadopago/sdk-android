package com.mercadopago.sdk.android.checkout.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

internal const val DEFAULT_INSTALLMENT_MIN = 1
internal const val DEFAULT_INSTALLMENT_MAX = 180

/**
 * Installment class, used to configure the installments
 * @param minInstallments Int
 * @param maxInstallments Int
 */
@Parcelize
data class Installment(
    val minInstallments: Int = DEFAULT_INSTALLMENT_MIN,
    val maxInstallments: Int = DEFAULT_INSTALLMENT_MAX,
) : Parcelable
