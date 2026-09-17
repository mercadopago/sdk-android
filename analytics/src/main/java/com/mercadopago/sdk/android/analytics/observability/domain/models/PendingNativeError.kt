package com.mercadopago.sdk.android.analytics.observability.domain.models

internal data class PendingNativeError(
    val eventId: String,
    val occurredAt: String,
    val error: NativeError,
)
