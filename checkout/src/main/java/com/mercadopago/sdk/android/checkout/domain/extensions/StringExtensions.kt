package com.mercadopago.sdk.android.checkout.domain.extensions

internal fun String?.orNullIfBlank(): String? = takeIf { !it.isNullOrBlank() }
