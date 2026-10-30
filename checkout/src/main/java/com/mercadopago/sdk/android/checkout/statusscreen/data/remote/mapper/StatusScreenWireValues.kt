package com.mercadopago.sdk.android.checkout.statusscreen.data.remote.mapper

internal enum class StatusType(
    val wireValue: String,
) {
    APPROVED("approved"),
    PENDING("pending"),
    REJECTED("rejected"),
}

internal enum class FooterButtonAction(
    val wireValue: String,
) {
    BACK("back_action"),
    OPEN_PDF("open_pdf"),
    CHANGE_PAYMENT_METHOD("change_payment_method"),
}
