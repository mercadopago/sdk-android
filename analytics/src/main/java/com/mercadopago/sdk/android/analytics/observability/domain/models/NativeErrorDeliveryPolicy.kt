package com.mercadopago.sdk.android.analytics.observability.domain.models

// @spec 20260825-native-coremethods-checkout-observability#DD-7

import androidx.annotation.RestrictTo

/** Selects native-error delivery independently for each SDK module. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
data class NativeErrorDeliveryPolicy(
    /** Delivery mode used by Core Methods operations. */
    val coreMethods: NativeErrorDeliveryMode = NativeErrorDeliveryMode.DUAL_WRITE,
    /** Delivery mode used by Checkout operations. */
    val checkout: NativeErrorDeliveryMode = NativeErrorDeliveryMode.DUAL_WRITE,
) {
    /** Returns the delivery mode configured for [module]. */
    fun modeFor(module: NativeErrorModule): NativeErrorDeliveryMode = when (module) {
        NativeErrorModule.CORE_METHODS -> coreMethods
        NativeErrorModule.CHECKOUT -> checkout
    }
}
