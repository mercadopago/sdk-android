package com.mercadopago.sdk.android.checkout.presentation.state

import com.mercadopago.sdk.android.checkout.core.model.CardBrand
import com.mercadopago.sdk.android.checkout.core.model.CardType

internal sealed class CardNumberErrorType {
    data class FieldValidation(val message: String) : CardNumberErrorType()

    data object PaymentMethodNotFound : CardNumberErrorType()

    data object LuhnValidation : CardNumberErrorType()

    data class CardBrandNotAccepted(val brand: CardBrand) : CardNumberErrorType()

    data class CardTypeNotAccepted(val cardType: CardType?) : CardNumberErrorType()
}
