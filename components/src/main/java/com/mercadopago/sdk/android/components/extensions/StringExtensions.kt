package com.mercadopago.sdk.android.components.extensions

import java.math.BigDecimal

internal fun String.isPositive(): Boolean {
    val digits = filter { it.isDigit() }
    return digits.toBigDecimalOrNull()?.let { it > BigDecimal.ZERO } ?: false
}
