package com.mercadopago.sdk.android.checkout.domain.extensions

import android.util.Patterns

internal fun String.isValidEmailFormat(): Boolean = isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
