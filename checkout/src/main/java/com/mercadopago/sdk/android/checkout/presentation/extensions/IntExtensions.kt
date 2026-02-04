package com.mercadopago.sdk.android.checkout.presentation.extensions


internal fun Int.toCountStringPlaceholder(placeHolder: String): String =
    "$placeHolder ${(1..this).joinToString("")}"
