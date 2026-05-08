package com.mercadopago.sdk.android.example.extensions

import java.text.NumberFormat
import java.util.Locale

fun Float.toCurrencyString(locale: Locale = Locale.getDefault()): String {
    val format = NumberFormat.getCurrencyInstance(locale)
    return format.format(this)
}
