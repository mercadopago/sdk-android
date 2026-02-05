package com.mercadopago.sdk.android.checkout.presentation.extensions

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

internal fun Float.toCurrencyString(locale: Locale = Locale.getDefault()): String {
    return NumberFormat.getCurrencyInstance(locale).format(this)
}

internal fun Float.toDecimalPartString(): String {
    val scaled = BigDecimal.valueOf(this.toDouble())
        .setScale(2, RoundingMode.HALF_UP)
    val cents = scaled.remainder(BigDecimal.ONE)
        .movePointRight(2)
        .abs()
        .toInt()
    return cents.toString().padStart(2, '0')
}
