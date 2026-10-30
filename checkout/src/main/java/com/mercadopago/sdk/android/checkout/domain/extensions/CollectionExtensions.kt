package com.mercadopago.sdk.android.checkout.domain.extensions

private const val SEPARATOR = ","

internal fun <T> List<T>.joinOrNull(
    transform: (T) -> String,
): String? = joinToString(SEPARATOR, transform = transform).takeIf { it.isNotEmpty() }

internal fun <T, R> List<T>.mapAllOrNull(
    transform: (T) -> R?,
): List<R>? {
    val mapped = map(transform)
    return if (mapped.any { it == null }) null else mapped.filterNotNull()
}
