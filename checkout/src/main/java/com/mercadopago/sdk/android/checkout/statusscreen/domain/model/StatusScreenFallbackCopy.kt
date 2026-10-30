package com.mercadopago.sdk.android.checkout.statusscreen.domain.model

internal const val PAYMENT_STATUS_REJECTED = "rejected"
internal const val PAYMENT_STATUS_ERROR = "error"
internal const val STATUS_FAILED = "failed"
internal const val ORDER_STATUS_REJECTED = "rejected"
internal const val ORDER_STATUS_ACTION_REQUIRED = "action_required"
internal val ORDER_STATUS_PENDING = setOf("pending", "processing", "waiting_payment")

internal data class StatusScreenFallbackCopy(
    val title: String,
    val ticketTitle: String,
    val paymentSubtitle: String?,
    val backLabel: String,
    val openUrlLabel: String,
    val barcodeLabel: String,
    val barcodeCopyLabel: String,
    val barcodeCopyFeedback: String,
    val iconRes: Int,
    val rejectedHeader: StatusScreenHeader,
    val errorHeader: StatusScreenHeader,
    val pendingHeader: StatusScreenHeader,
)
