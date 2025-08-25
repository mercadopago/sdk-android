package com.mercadopago.sdk.android.coremethods.domain.model

/**
 * Represents a tokenized card for secure payment processing.
 * This class contains the token generated after successful card tokenization,
 * which is used to process payments without handling sensitive card data directly.
 *
 * @param token The secure token representing the card, generated after successful tokenization
 * @param publicKey This app public Key
 * @param cardId This card id
 * @param firstSixDigits First card six digits
 * @param expirationMonth Card expiration Month
 * @param expirationYear Card expiration year
 * @param lastFourDigits Last four digits of the card
 * @param cardHolder Card holder
 * @param status Card status
 * @param dateCreated Date of creation of this token
 * @param dateLastUpdated Date of creation of this token
 * @param dateDue Date of creation of this token
 * @param luhnValidation Card number has passed luhn Validation
 * @param liveMode This is live mode
 * @param requireEsc This require esc
 * @param cardNumberLength Card number length
 * @param securityCodeLength  Card security code length
 * @param truncCardNumber Thunc Card Number
 */
data class CardToken(
    val token: String,
    val publicKey: String? = null,
    val cardId: String? = null,
    val firstSixDigits: String? = null,
    val expirationMonth: Int? = null,
    val expirationYear: Int? = null,
    val lastFourDigits: String? = null,
    val cardHolder: CardHolder? = null,
    val status: String? = null,
    val dateCreated: String? = null,
    val dateLastUpdated: String? = null,
    val dateDue: String? = null,
    val luhnValidation: Boolean? = null,
    val liveMode: Boolean? = null,
    val requireEsc: Boolean? = null,
    val cardNumberLength: Int? = null,
    val securityCodeLength: Int? = null,
    val truncCardNumber: String? = null,
)

/**
 * CardHolder class
 * @param identification [Identification] type of this card holder
 * @param name: value of this identification
 */
data class CardHolder(
    val identification: Identification? = null,
    val name: String? = null,
)

/**
 * Identification class
 * @param type The type of the identification
 */
data class Identification(
    val type: String? = null,
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
