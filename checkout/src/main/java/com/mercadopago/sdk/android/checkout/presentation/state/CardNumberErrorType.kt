package com.mercadopago.sdk.android.checkout.presentation.state

import com.mercadopago.sdk.android.checkout.core.model.CardBrand
import com.mercadopago.sdk.android.checkout.core.model.CardType

internal sealed class CardNumberErrorType {
    data object None : CardNumberErrorType()

    data object FieldValidation : CardNumberErrorType()

    data object PaymentMethodNotFound : CardNumberErrorType()

    data object LuhnValidation : CardNumberErrorType()

    data class CardBrandNotAccepted(val brand: CardBrand) : CardNumberErrorType()

    data class CardTypeNotAccepted(val cardType: CardType?) : CardNumberErrorType()
}

internal fun CardNumberErrorType.isPaymentNotFound() = this == CardNumberErrorType.PaymentMethodNotFound

internal fun CardNumberErrorType.isFieldValidationOrNone(): Boolean =
    this == CardNumberErrorType.FieldValidation || this == CardNumberErrorType.None
