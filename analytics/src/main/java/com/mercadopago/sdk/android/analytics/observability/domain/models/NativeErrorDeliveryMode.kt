package com.mercadopago.sdk.android.analytics.observability.domain.models

// @spec 20260825-native-coremethods-checkout-observability#DD-7

internal enum class NativeErrorDeliveryMode {
    MELIDATA_ONLY,
    DUAL_WRITE,
    OBSERVABILITY_ONLY;

    companion object {
        fun from(value: String): NativeErrorDeliveryMode =
            values().firstOrNull { it.name == value } ?: MELIDATA_ONLY
    }
}
