package com.mercadopago.sdk.android.components.extensions

fun Float.scrollProgressRatio(divisor: Float): Float =
    if (divisor > 0) (this / divisor).coerceIn(0f, 1f) else 0f
