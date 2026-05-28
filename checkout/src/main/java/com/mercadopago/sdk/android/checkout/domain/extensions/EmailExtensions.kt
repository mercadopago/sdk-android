package com.mercadopago.sdk.android.checkout.domain.extensions

import android.util.Patterns

internal fun String.isInvalidEmailFormat(): Boolean = isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(this).matches()
