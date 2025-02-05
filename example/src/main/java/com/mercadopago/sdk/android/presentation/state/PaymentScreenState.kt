package com.mercadopago.sdk.android.presentation.state

data class PaymentScreenViewState(
    val expirationDateState: ExpirationDateState = ExpirationDateState(),
    val secureCodeState: SecurityCodeState = SecurityCodeState(),
    val cardNumberState: CardNumberTextFieldState = CardNumberTextFieldState()
)

data class SecurityCodeState(
    val isFocused: Boolean = false,
    val filled: Boolean = false,
    val length: Int = 0,
)

data class ExpirationDateState(
    val isFocused: Boolean = false,
    val filled: Boolean = false,
    val length: Int = 0,
    val valid: Boolean = false
)

data class CardNumberTextFieldState(
    var isFocused: Boolean = false,
    var filled: Boolean = false,
    var length: Int = 0,
    val isValid: Boolean = false,
    val lastFourDigits: String = "",
    val cardBin: String? = null,
)
