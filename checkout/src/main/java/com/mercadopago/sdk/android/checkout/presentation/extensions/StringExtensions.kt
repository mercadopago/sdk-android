package com.mercadopago.sdk.android.checkout.presentation.extensions

import com.mercadopago.sdk.android.checkout.presentation.state.AmountParts
import com.mercadopago.sdk.android.components.MPAmountData

internal const val ZERO = "0"

internal fun String.hasAllSameDigits(): Boolean {
    val digits = this.filter { it.isDigit() }
    return digits.isNotEmpty() && digits.all { it == digits.first() }
}

internal fun String.isBeingCleared(
    previousValue: String,
): Boolean = this.length < previousValue.length

internal fun String?.getOrZero() = this ?: ZERO

internal fun String.toBrandLabel(): String =
    split('_')
        .filter { it.isNotEmpty() }
        .joinToString(separator = " ") { it.replaceFirstChar(Char::uppercaseChar) }

internal fun String.extractAmountDigits(): String =
    replace(".", "")
        .replace(",", "")
        .filter { it.isDigit() }

internal fun String.toAmountParts(
    currencySymbol: String,
): AmountParts {
    val numeric = removePrefix(currencySymbol).trim()
    val lastSeparator = numeric.lastIndexOfAny(charArrayOf('.', ','))
    val hasDecimal = lastSeparator > 0 &&
        lastSeparator < numeric.length - 1 &&
        numeric.substring(lastSeparator + 1).let { after -> after.all(Char::isDigit) && after.length == 2 }
    return if (hasDecimal) {
        AmountParts(
            currencySymbol = currencySymbol,
            integerPart = numeric.substring(0, lastSeparator),
            decimalPart = numeric.substring(lastSeparator + 1),
        )
    } else {
        AmountParts(currencySymbol = currencySymbol, integerPart = numeric, decimalPart = "")
    }
}

internal fun String.toAmountParts(): AmountParts {
    val trimmed = trim()
    val currencySymbol = trimmed.takeWhile { !it.isDigit() }.trim().ifEmpty { "$" }
    return toAmountParts(currencySymbol)
}

internal fun AmountParts.toMPAmountData(): MPAmountData =
    MPAmountData(
        currencySymbol = currencySymbol,
        integerPart = integerPart,
        decimalPart = decimalPart,
    )
