package com.mercadopago.sdk.android.checkout.presentation.state

import com.mercadopago.sdk.android.checkout.core.model.MPCardBrand
import com.mercadopago.sdk.android.checkout.core.model.MPCardType

internal sealed class CardNumberErrorType {
    data class FieldValidation(val message: String) : CardNumberErrorType()

    data class PaymentMethodNotFound(val message: String) : CardNumberErrorType()

    data object LuhnValidation : CardNumberErrorType()

    data class CardBrandNotAccepted(val brand: MPCardBrand) : CardNumberErrorType()

    data class CardTypeNotAccepted(val cardType: MPCardType?) : CardNumberErrorType()
}
