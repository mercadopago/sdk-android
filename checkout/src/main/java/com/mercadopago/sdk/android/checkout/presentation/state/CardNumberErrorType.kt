package com.mercadopago.sdk.android.checkout.presentation.state

internal sealed class CardNumberErrorType {
    data class FieldValidation(val message: String) : CardNumberErrorType()

    data class PaymentMethodNotFound(val message: String) : CardNumberErrorType()

    data object LuhnValidation : CardNumberErrorType()
}
