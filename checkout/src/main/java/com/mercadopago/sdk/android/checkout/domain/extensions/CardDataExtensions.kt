package com.mercadopago.sdk.android.checkout.domain.extensions

import com.mercadopago.android.sdk.checkout.R
import com.mercadopago.sdk.android.checkout.domain.model.CardData
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCode
import com.mercadopago.sdk.android.checkout.domain.provider.StringProvider

private const val SECURITY_CODE_LENGTH_FOUR = 4
private const val SECURITY_CODE_LENGTH_ZERO = 0

internal fun SecurityCode.isOptional(): Boolean = length <= SECURITY_CODE_LENGTH_ZERO

internal fun CardData.getLength(): Int = paymentMethod.card?.length?.max ?: CARD_LENGTH_19

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

internal fun SecurityCode.getPlaceholder(
    stringProvider: StringProvider,
): String =
    when (length) {
        SECURITY_CODE_LENGTH_FOUR -> stringProvider.getString(R.string.card_form_security_placeholder_four_digits)
        else -> stringProvider.getString(R.string.card_form_security_placeholder_three_digits)
    }
