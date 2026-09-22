package com.mercadopago.sdk.android.analytics.observability.domain.models

// @spec 20260825-native-coremethods-checkout-observability#DD-7

internal data class NativeErrorDeliveryPolicy(
    val coreMethods: NativeErrorDeliveryMode = NativeErrorDeliveryMode.DUAL_WRITE,
    val checkout: NativeErrorDeliveryMode = NativeErrorDeliveryMode.DUAL_WRITE,
) {
    fun modeFor(module: NativeErrorModule): NativeErrorDeliveryMode = when (module) {
        NativeErrorModule.CORE_METHODS -> coreMethods
        NativeErrorModule.CHECKOUT -> checkout
    }
}
