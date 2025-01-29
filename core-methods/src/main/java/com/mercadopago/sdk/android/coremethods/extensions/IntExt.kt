package com.mercadopago.sdk.android.coremethods.extensions

import kotlin.math.max
import kotlin.math.min

/**
 * Return the number between a range
 * @param min: minimal range
 * @param max: maximum range
 */
internal fun Int.between(
    min: Int,
    max: Int,
): Int = max(min, min(this, max))

/**
 * Take the last digits by count
 * @param num: digits to take
 */
internal fun Int.takeLast(num: Int): Int = this.toString().takeLast(num).toInt()
