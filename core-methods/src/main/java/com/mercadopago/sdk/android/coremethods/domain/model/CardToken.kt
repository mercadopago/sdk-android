package com.mercadopago.sdk.android.coremethods.domain.model

/**
 * CardToken
 * @param token: token of the card
 */
data class CardToken(
    val token: String,
)

/**
 * Buyer Identification
 * @param name: is the buyer identification name
 * @param number: is the buyer identification number
 * @param type: is the buyer identification type
 */
data class BuyerIdentification(
    val name: String?,
    val number: String?,
    val type: String?
)
