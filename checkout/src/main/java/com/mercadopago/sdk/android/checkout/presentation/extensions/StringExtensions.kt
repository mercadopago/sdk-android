package com.mercadopago.sdk.android.checkout.presentation.extensions

import java.text.NumberFormat
import java.util.Locale

internal fun Locale?.getCurrencyString(): String {
    val locale = this ?: Locale.getDefault()
    return NumberFormat.getCurrencyInstance(locale).currency?.symbol.orEmpty()
}

internal fun String.hasAllSameDigits(): Boolean {
    val digits = this.filter { it.isDigit() }
    return digits.isNotEmpty() && digits.all { it == digits.first() }
}
