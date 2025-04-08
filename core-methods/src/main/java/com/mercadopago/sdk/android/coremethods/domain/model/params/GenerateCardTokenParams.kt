package com.mercadopago.sdk.android.coremethods.domain.model.params

internal data class GenerateCardTokenParams(
    val cardId: String? = null,
    val esc: String? = null,
    val requireEsc: Boolean = false,
    val cardNumber: String? = null,
    val securityCode: String? = null,
    val expirationMonth: Int? = null,
    val expirationYear: Int? = null,
    val buyerIdentification: BuyerIdentification? = null,
    val device: String? = null
)

internal data class BuyerIdentification(
    val name: String,
    val number: String,
    val type: String,
)
