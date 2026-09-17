package com.mercadopago.sdk.android.analytics.observability.domain.classifier

// @spec 20260825-native-coremethods-checkout-observability#DD-4

private const val MAX_CORRELATION_ID_LENGTH = 128
private val SAFE_CORRELATION_ID = Regex("[A-Za-z0-9._:-]+")

internal fun Int.validNativeHttpStatus(): Int? =
    takeIf { this in NativeErrorHttpStatusCodes.MIN_VALID..NativeErrorHttpStatusCodes.MAX_VALID }

internal fun String.validNativeCorrelationId(): String? = takeIf {
    length in 1..MAX_CORRELATION_ID_LENGTH && SAFE_CORRELATION_ID.matches(this)
}
