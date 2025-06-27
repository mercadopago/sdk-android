package com.mercadopago.sdk.android.coremethods.domain.model

/**
 * Represents a tokenized card for secure payment processing.
 * This class contains the token generated after successful card tokenization,
 * which is used to process payments without handling sensitive card data directly.
 *
 * @param token The secure token representing the card, generated after successful tokenization
 */
data class CardToken(
    val token: String,
)

@Suppress("MaxLineLength")
/**
 * Represents the buyer's identification information for payment processing.
 * This class contains the necessary identification details of the cardholder,
 * including name and optional identification number and type.
 *
 * Example:
 * ```kotlin
 * // Create buyer identification for testing approved payment
 * val buyerId = BuyerIdentification(
 *     name = "APRO",
 *     number = "123456789",
 *     type = "CPF"
 * )
 *
 * // Create buyer identification for testing error payment
 * val realBuyerId = BuyerIdentification(
 *     name = "OTHE",
 *     number = "123456789",
 *     type = "CPF"
 * )
 * ```
 *
 * @param name The cardholder's name.
 * @param number Optional identification number of the cardholder
 * @param type Optional identification type of the cardholder
 * @see <a href="https://www.mercadopago.com.br/developers/en/docs/checkout-api/additional-content/your-integrations/test/cards">Test cards</a>
 *
 */
data class BuyerIdentification(
    val name: String,
    val number: String? = null,
    val type: String? = null,
)
