package com.mercadopago.sdk.android.checkout.domain.extensions

import com.mercadopago.android.sdk.checkout.R
import com.mercadopago.sdk.android.checkout.core.model.CardBrand
import com.mercadopago.sdk.android.checkout.core.model.CardType
import com.mercadopago.sdk.android.checkout.domain.model.CardData
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCode
import com.mercadopago.sdk.android.checkout.domain.provider.StringProvider
import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberErrorType
import com.mercadopago.sdk.android.checkout.presentation.state.DEFAULT_MAX_CARD_LENGTH

internal fun SecurityCode.isOptional(): Boolean = length <= 0

internal fun CardData.getLength(): Int = paymentMethod.card?.length?.max ?: DEFAULT_MAX_CARD_LENGTH

internal const val NOT_FOUND = "not found"

internal fun SecurityCode.getMessage(
    stringProvider: StringProvider,
): String {
    val stringRes = if (location == "back") {
        R.string.card_form_security_code_tooltip_back
    } else {
        R.string.card_form_security_code_tooltip_front
    }
    return stringProvider.getString(stringRes).format(length)
}

internal fun detectCardNumberErrorType(
    errorMessage: String,
    stringProvider: StringProvider,
): CardNumberErrorType {
    val brandErrorPrefix = stringProvider.getString(R.string.card_form_error_card_brand_not_accepted)
    val typeErrorPrefix = stringProvider.getString(R.string.card_form_error_card_type_not_accepted)

    return when {
        errorMessage.contains(NOT_FOUND, ignoreCase = true) -> {
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
