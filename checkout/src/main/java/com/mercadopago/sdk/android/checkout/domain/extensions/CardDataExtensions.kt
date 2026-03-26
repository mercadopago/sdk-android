package com.mercadopago.sdk.android.checkout.domain.extensions

import com.mercadopago.android.sdk.checkout.R
import com.mercadopago.sdk.android.checkout.domain.model.CardData
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCode
import com.mercadopago.sdk.android.checkout.domain.provider.StringProvider

internal fun SecurityCode.isOptional(): Boolean = length <= 0

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
