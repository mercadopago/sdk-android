package com.mercadopago.sdk.android.checkout.presentation.state

import com.mercadopago.sdk.android.checkout.core.model.MPCardBrand
import com.mercadopago.sdk.android.checkout.core.model.MPCardType

/** Represents the possible error types for card number validation in the card payment flow. */
internal sealed class CardNumberErrorType {
    internal data class FieldValidation(val message: String) : CardNumberErrorType()

    internal data class PaymentMethodNotFound(val message: String) : CardNumberErrorType()

    internal data object LuhnValidation : CardNumberErrorType()

    internal data class CardBrandNotAccepted(val brand: MPCardBrand) : CardNumberErrorType()

    internal data class CardTypeNotAccepted(val cardType: MPCardType?) : CardNumberErrorType()
}
