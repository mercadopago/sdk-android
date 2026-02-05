package com.mercadopago.sdk.android.checkout.presentation.extensions

import java.text.NumberFormat
import java.util.Locale

internal fun Locale?.getCurrencyString(): String {
    val locale = this ?: Locale.getDefault()
    return NumberFormat.getCurrencyInstance(locale).currency?.symbol.orEmpty()
}
