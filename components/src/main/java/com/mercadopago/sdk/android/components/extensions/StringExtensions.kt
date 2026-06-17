package com.mercadopago.sdk.android.components.extensions

import java.math.BigDecimal

internal fun String.isGreaterThan(
    value: BigDecimal = BigDecimal.ZERO,
): Boolean {
    val digits = filter { it.isDigit() }
    return digits.toBigDecimalOrNull()?.let { it > value } ?: false
}
