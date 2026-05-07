package com.mercadopago.sdk.android.example.extensions

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

fun BigDecimal.toCurrencyString(locale: Locale = Locale.getDefault()): String {
    val format = NumberFormat.getCurrencyInstance(locale)
    return format.format(this)
}
