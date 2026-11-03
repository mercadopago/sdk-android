package com.mercadopago.sdk.android.components.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.math.BigDecimal

private const val BASE64_PNG_DATA_URI_PREFIX = "data:image/png;base64,"
private const val MAX_ENCODED_LENGTH = 200_000

internal fun String.isGreaterThan(
    value: BigDecimal = BigDecimal.ZERO,
): Boolean {
    val digits = filter { it.isDigit() }
    return digits.toBigDecimalOrNull()?.let { it > value } ?: false
}

internal fun String?.asBase64PngPayloadOrNull(): String? =
    this
        ?.takeIf { it.length <= MAX_ENCODED_LENGTH && it.startsWith(BASE64_PNG_DATA_URI_PREFIX) }
        ?.removePrefix(BASE64_PNG_DATA_URI_PREFIX)

internal fun String?.toValidatedBitmapOrNull(): Bitmap? =
    asBase64PngPayloadOrNull()
        ?.let { runCatching { Base64.decode(it, Base64.DEFAULT) }.getOrNull() }
        ?.takeIf { bytes -> bytes.hasValidPngBounds() }
        ?.let { bytes -> BitmapFactory.decodeByteArray(bytes, 0, bytes.size) }
