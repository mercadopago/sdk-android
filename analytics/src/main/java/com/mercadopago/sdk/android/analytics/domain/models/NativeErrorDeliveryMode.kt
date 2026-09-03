package com.mercadopago.sdk.android.analytics.domain.models

/** Controls which observability destinations receive native errors. */
internal enum class NativeErrorDeliveryMode {
    MELIDATA_ONLY,
    DUAL_WRITE,
    OBSERVABILITY_ONLY;

    companion object {
        /** Parses a build-time mode and fails closed to Melidata-only delivery. */
        fun from(value: String): NativeErrorDeliveryMode =
            values().firstOrNull { it.name == value } ?: MELIDATA_ONLY
    }
}
