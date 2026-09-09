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

/** Immutable release policy with independently configurable SDK modules. */
internal data class NativeErrorDeliveryPolicy(
    val coreMethods: NativeErrorDeliveryMode = NativeErrorDeliveryMode.DUAL_WRITE,
    val checkout: NativeErrorDeliveryMode = NativeErrorDeliveryMode.DUAL_WRITE,
) {
    fun modeFor(module: NativeErrorModule): NativeErrorDeliveryMode = when (module) {
        NativeErrorModule.CORE_METHODS -> coreMethods
        NativeErrorModule.CHECKOUT -> checkout
    }
}
