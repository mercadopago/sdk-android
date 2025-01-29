package com.mercadopago.sdk.android.presentation.state

data class PaymentScreenViewState(
    val expirationDateState: ExpirationDateState = ExpirationDateState(),
    val secureCodeState: SecurityCodeState = SecurityCodeState()
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
