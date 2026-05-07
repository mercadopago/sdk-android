package com.mercadopago.sdk.android.checkout.presentation.extensions

import java.text.NumberFormat
import java.util.Locale

internal const val ZERO = "0"

internal fun Locale?.getCurrencyString(): String {
    val locale = this ?: Locale.getDefault()
    return NumberFormat.getCurrencyInstance(locale).currency?.symbol.orEmpty()
}

internal fun String.hasAllSameDigits(): Boolean {
    val digits = this.filter { it.isDigit() }
    return digits.isNotEmpty() && digits.all { it == digits.first() }
}

internal fun String.isBeingCleared(
    previousValue: String,
): Boolean = this.length < previousValue.length

internal fun String?.getOrZero() = this ?: ZERO
