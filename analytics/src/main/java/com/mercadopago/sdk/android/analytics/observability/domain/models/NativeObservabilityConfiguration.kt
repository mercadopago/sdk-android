package com.mercadopago.sdk.android.analytics.observability.domain.models

internal data class NativeObservabilityConfiguration(
    val sdkName: String,
    val sdkVersion: String,
    val siteId: String,
    val deliveryPolicy: NativeErrorDeliveryPolicy,
)
