package com.mercadopago.sdk.android.checkout.statusscreen.domain.model

internal data class StatusScreenSubtitleSegment(
    val text: String,
    val state: StatusScreenSegmentState? = null,
)

internal enum class StatusScreenSegmentState {
    Success,
}
