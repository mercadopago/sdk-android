package com.mercadopago.sdk.android.checkout.domain.extensions

private const val SEPARATOR = ","

internal fun <T> List<T>.joinOrNull(
    transform: (T) -> String,
): String? = joinToString(SEPARATOR, transform = transform).takeIf { it.isNotEmpty() }
