package com.mercadopago.sdk.android.checkout.domain.extensions

import com.mercadopago.android.sdk.checkout.R
import com.mercadopago.sdk.android.checkout.core.model.CardBrand
import com.mercadopago.sdk.android.checkout.core.model.CardType
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.provider.StringProvider
import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberErrorType
import com.mercadopago.sdk.android.checkout.presentation.state.isPaymentNotFound

internal const val NOT_FOUND = "not found"
internal const val UNABLE_RESOURCE = "Unable to acquire requested payment method from the resource"

internal fun detectCardNumberErrorType(
    errorMessage: String,
    stringProvider: StringProvider,
): CardNumberErrorType {
    val brandErrorPrefix = stringProvider.getString(R.string.card_form_error_card_brand_not_accepted)
    val typeErrorPrefix = stringProvider.getString(R.string.card_form_error_card_type_not_accepted)

    return when {
        errorMessage.isPaymentMethodNotFound() -> {
            CardNumberErrorType.PaymentMethodNotFound
        }
        errorMessage.startsWith(brandErrorPrefix) -> {
            val brandString = errorMessage.removePrefix(brandErrorPrefix).trim()
            val brand = CardBrand.fromString(brandString)
            CardNumberErrorType.CardBrandNotAccepted(brand)
        }
        errorMessage.startsWith(typeErrorPrefix) -> {
            val typeString = errorMessage.removePrefix(typeErrorPrefix).trim()
            val cardType = CardType.fromString(typeString)
            CardNumberErrorType.CardTypeNotAccepted(cardType)
        }
        else -> CardNumberErrorType.None
    }
}

internal fun CardNumberErrorType.getErrorMessage(
    stringProvider: StringProvider,
    fallbackMessage: String,
): String =
    if (isPaymentNotFound()) {
        stringProvider.getString(R.string.card_form_error_card_number_repeated)
    } else {
        fallbackMessage
    }

internal fun String.isPaymentMethodNotFound(): Boolean =
    this.contains(NOT_FOUND, ignoreCase = true) ||
        this.contains(UNABLE_RESOURCE, ignoreCase = true)

internal fun MercadoPagoCheckoutError.ServiceError.getCardNumberErrorInfo(
    stringProvider: StringProvider,
): Pair<CardNumberErrorType, String> {
    val errorType = detectCardNumberErrorType(
        errorMessage = this.errorMessage,
        stringProvider = stringProvider,
    )
    val message = errorType.getErrorMessage(
        stringProvider = stringProvider,
        fallbackMessage = this.errorMessage,
    )
    return errorType to message
}
