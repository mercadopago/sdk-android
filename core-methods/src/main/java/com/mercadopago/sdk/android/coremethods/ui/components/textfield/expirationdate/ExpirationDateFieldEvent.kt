package com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate

interface ExpirationDateFieldEvent {

    data class FocusChanged(val isFocused: Boolean) : ExpirationDateFieldEvent

    data class Filled(val isFilled: Boolean) : ExpirationDateFieldEvent

    data class Length(val length: Int) : ExpirationDateFieldEvent
}
