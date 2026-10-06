package com.mercadopago.sdk.android.analytics.observability.domain.models

// @spec 20260825-native-coremethods-checkout-observability#DD-7

import androidx.annotation.RestrictTo

/** Determines which telemetry destinations receive a classified native error. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
enum class NativeErrorDeliveryMode {
    /** Emit only the existing Melidata metric. */
    MELIDATA_ONLY,

    /** Emit both the existing Melidata metric and the observability metric. */
    DUAL_WRITE,

    /** Emit only the observability metric. */
    OBSERVABILITY_ONLY;

    /** Factory functions for delivery modes. */
    companion object {
        /**
         * Resolves a configuration value, defaulting safely to [MELIDATA_ONLY].
         */
        fun from(value: String): NativeErrorDeliveryMode =
            values().firstOrNull { it.name == value } ?: MELIDATA_ONLY
    }
}
