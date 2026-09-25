package com.mercadopago.sdk.android.analytics.observability.domain.models

import androidx.annotation.RestrictTo

/** Immutable runtime inputs required to construct the native observability reporter. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
data class NativeObservabilityConfiguration(
    /** SDK product identifier included in observability requests. */
    val sdkName: String,
    /** SDK version included in observability requests. */
    val sdkVersion: String,
    /** Mercado Pago site identifier associated with this SDK session. */
    val siteId: String,
    /** Per-module decision for legacy and observability delivery. */
    val deliveryPolicy: NativeErrorDeliveryPolicy,
)
