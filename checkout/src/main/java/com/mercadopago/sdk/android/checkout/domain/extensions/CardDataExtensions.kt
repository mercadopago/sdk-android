package com.mercadopago.sdk.android.checkout.domain.extensions

import com.mercadopago.sdk.android.checkout.domain.model.CardData
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCode

private const val SECURITY_CODE_LENGTH_ZERO = 0

internal fun SecurityCode.isOptional(): Boolean = length <= SECURITY_CODE_LENGTH_ZERO

internal fun CardData.getLength(): Int = paymentMethod.card?.length?.max ?: CARD_LENGTH_19
