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
 * @param name is the buyer identification name (To test different payment results, fill
 * in the desired status in the cardholder's name, example: APRO, OTHE, CONT)
 * @param number is the buyer cardholder identification number
 * @param type is the buyer cardholder identification type
 * @see
 * <a href="https://www.mercadopago.com.br/developers/en/docs/checkout-api/
 * additional-content/your-integrations/test/cards">Test cards</a>
 */
data class BuyerIdentification(
    val name: String?,
    val number: String?,
    val type: String?
)
