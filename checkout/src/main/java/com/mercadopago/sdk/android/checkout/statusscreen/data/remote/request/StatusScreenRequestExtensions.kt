package com.mercadopago.sdk.android.checkout.statusscreen.data.remote.request

private val LAST_FOUR_DIGITS_PATTERN = Regex("^\\d{4}$")

internal fun String?.asLastFourDigitsOrNull(): String? =
    takeIf {
        !it.isNullOrBlank() && LAST_FOUR_DIGITS_PATTERN.matches(it)
    }
