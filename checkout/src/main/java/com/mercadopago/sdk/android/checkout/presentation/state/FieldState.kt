package com.mercadopago.sdk.android.checkout.presentation.state

internal sealed interface FieldState {
    val label: String
    val helper: String
    val placeHolder: String
    val error: String
    val isFocused: Boolean
    val filled: Boolean
    val enabled: Boolean
    val isValid: Boolean
    val showPlaceHolder: Boolean
}
