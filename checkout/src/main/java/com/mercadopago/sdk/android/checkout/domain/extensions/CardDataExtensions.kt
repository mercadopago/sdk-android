package com.mercadopago.sdk.android.checkout.domain.extensions

import com.mercadopago.sdk.android.checkout.domain.model.CardData
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCode
import com.mercadopago.sdk.android.checkout.presentation.state.DEFAULT_MAX_CARD_LENGTH

internal fun SecurityCode.isOptional(): Boolean = length <= 0

internal fun CardData.getLength(): Int = paymentMethod.card?.length?.max ?: DEFAULT_MAX_CARD_LENGTH

internal fun SecurityCode.getMessage(): String {
    val location = if (location == "back") {
        "no verso"
    } else {
        "na parte da frente"
    }
    return "É um número de $length dígitos que está $location do seu cartão."
}
