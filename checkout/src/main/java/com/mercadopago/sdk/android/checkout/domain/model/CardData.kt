package com.mercadopago.sdk.android.checkout.domain.model

import com.mercadopago.sdk.android.coremethods.domain.model.CardIssuer
import com.mercadopago.sdk.android.coremethods.domain.model.Installment
import com.mercadopago.sdk.android.coremethods.domain.model.PaymentMethod

internal data class CardData(
    val paymentMethod: PaymentMethod,
    val securityCode: SecurityCode,
    val cardIssuer: CardIssuer?,
    val installments: List<Installment>?,
)

internal data class SecurityCode(
    val length: Int,
    val mode: String,
    val location: String,
)
