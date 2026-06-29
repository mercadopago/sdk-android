package com.mercadopago.sdk.android.checkout.presentation.extensions

internal fun Int.toCountStringPlaceholder(
    placeHolder: String,
): String = "$placeHolder ${(1..this).joinToString("")}"

internal fun Int.isBeingCleared(
    previousLength: Int,
): Boolean = this < previousLength

internal fun Int.isEmpty(): Boolean = this == 0
