package com.mercadopago.sdk.android.checkout.presentation.model

internal enum class CancelReason(val analyticsValue: String) {
    SystemBack("user_tapped_back_button"),
    UiButton("user_tapped_ui_back_button"),
}
