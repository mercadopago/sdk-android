package com.mercadopago.sdk.android.coremethods.domain.model

/**
 * Represents a tokenized card for secure payment processing.
 * This class contains the token generated after successful card tokenization,
 * which is used to process payments without handling sensitive card data directly.
 *
 * @param token The secure token representing the card, generated after successful tokenization
 *
 * Example:
 * ```kotlin
 * // Create a card token from tokenization response
 * val cardToken = CardToken(
 *     token = "1234567890abcdef"
 * )
 *
 * // Use the token for payment processing
 * processPayment(cardToken.token)
 * ```
 */
data class CardToken(
    val token: String,
)

/**
 * Represents the buyer's identification information for payment processing.
 * This class contains the necessary identification details of the cardholder,
 * including name and optional identification number and type.
 * For testing purposes, specific status codes can be included in the name
 * to simulate different payment scenarios.
 *
 * @param name The cardholder's name. For testing, include status codes like:
 *            - APRO: Approved payment
 *            - OTHE: Other payment status
 *            - CONT: Contingent payment
 * @param number Optional identification number of the cardholder
 * @param type Optional identification type of the cardholder
 *
 * Example:
 * ```kotlin
 * // Create buyer identification for testing
 * val buyerId = BuyerIdentification(
 *     name = "APRO John Doe",
 *     number = "123456789",
 *     type = "CPF"
 * )
 *
 * // Create buyer identification for production
 * val realBuyerId = BuyerIdentification(
 *     name = "John Doe",
 *     number = "123456789",
 *     type = "CPF"
 * )
 * ```
 *
 * @see
 * <a href="https://www.mercadopago.com.br/developers/en/docs/checkout-api/
 * additional-content/your-integrations/test/cards">Test cards</a>
 */
data class BuyerIdentification(
    val name: String,
    val number: String? = null,
    val type: String? = null
)
