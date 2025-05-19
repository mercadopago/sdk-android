package com.mercadopago.sdk.android.coremethods.domain.model

/**
 * Represents a payment method with comprehensive details for financial transactions.
 * This class encapsulates all necessary information about a payment method including
 * its processing rules, validation requirements, and associated costs.
 * It supports various payment types like credit cards, debit cards, and other payment methods.
 *
 * @param financialInstitution List of financial institutions that can process this payment method
 * @param payerCost List of available payment costs and installment options
 * @param issuer The financial institution that issues this payment method
 * @param totalFinancialCost Total cost including fees and taxes for using this payment method
 * @param minAccreditationDays Minimum days required for payment accreditation
 * @param maxAccreditationDays Maximum days required for payment accreditation
 * @param merchantAccountId Unique identifier for the merchant's account
 * @param id Unique identifier for this payment method (e.g., "visa", "master")
 * @param paymentTypeId Type of payment (e.g., "credit_card", "debit_card")
 * @param accreditationTime Time required for payment accreditation
 * @param card Card-specific details and validation rules
 * @param thumbnail URL to the payment method's logo or icon
 * @param bins List of valid BINs (Bank Identification Numbers) for this payment method
 * @param marketplace Identifier for the marketplace where this payment method is available
 * @param deferredCapture Indicates if payment capture can be deferred
 * @param agreements List of agreements and terms associated with this payment method
 * @param labels Additional labels or tags for the payment method
 * @param siteId Identifier for the site/market where this payment method is used
 * @param processingMode Processing mode (e.g., "aggregator", "gateway")
 * @param additionalInfoNeeded List of additional information required for processing
 * @param status Current status of the payment method
 *
 * Example:
 * ```kotlin
 * val paymentMethod = PaymentMethod(
 *     id = "visa",
 *     paymentTypeId = "credit_card",
 *     card = CardModel(
 *         bin = 411111,
 *         length = LengthModel(min = 16, max = 16),
 *         securityCode = SecurityCodeModel(
 *             mode = "mandatory",
 *             length = 3
 *         )
 *     )
 * )
 * ```
 *
 * @see FinancialInstitutionModel
 * @see CardModel
 * @see PayerCost
 * @see Agreements
 *
 */
data class PaymentMethod(
    val financialInstitution: List<FinancialInstitutionModel>? = null,
    val payerCost: List<PayerCost>? = null,
    val issuer: Issuer? = null,
    val totalFinancialCost: String? = null,
    val minAccreditationDays: String? = null,
    val maxAccreditationDays: String? = null,
    val merchantAccountId: String? = null,
    val id: String? = null,
    val paymentTypeId: String? = null,
    val accreditationTime: String? = null,
    val card: CardModel? = null,
    val thumbnail: String? = null,
    val bins: List<Int>? = null,
    val marketplace: String? = null,
    val deferredCapture: String? = null,
    val agreements: List<Agreements>? = null,
    val labels: List<String>? = null,
    val siteId: String? = null,
    val processingMode: String? = null,
    val additionalInfoNeeded: List<String>? = null,
    val status: String? = null,
)

/**
 * Represents a financial institution that processes payments.
 * This class contains information about banks or financial entities
 * that are authorized to process transactions for a payment method.
 *
 * @param id Unique identifier for the financial institution
 * @param description Human-readable name or description of the institution
 *
 * Example:
 * ```kotlin
 * val institution = FinancialInstitutionModel(
 *     id = "123",
 *     description = "Example Bank"
 * )
 * ```
 */
data class FinancialInstitutionModel(
    val id: String?,
    val description: String,
)

/**
 * Represents detailed card information and validation rules.
 * This class contains all card-specific details including BIN,
 * length requirements, and security code specifications.
 * It is used to validate card information before processing.
 *
 * @param bin Bank Identification Number (first 6 digits of card)
 * @param length Card number length requirements
 * @param validation Validation method to be used for the card
 * @param securityCode Security code (CVV) requirements
 *
 * Example:
 * ```kotlin
 * val card = CardModel(
 *     bin = 411111,
 *     length = LengthModel(min = 16, max = 16),
 *     validation = "standard",
 *     securityCode = SecurityCodeModel(
 *         mode = "mandatory",
 *         length = 3
 *     )
 * )
 * ```
 *
 * @see LengthModel
 * @see SecurityCodeModel
 */
data class CardModel(
    val bin: Int? = null,
    val length: LengthModel? = null,
    val validation: String? = null,
    val securityCode: SecurityCodeModel? = null,
)

/**
 * Defines the length requirements for a card number.
 * This class specifies the valid length range for card numbers
 * to ensure proper validation of card information.
 *
 * @param min Minimum allowed length for the card number
 * @param max Maximum allowed length for the card number
 *
 * Example:
 * ```kotlin
 * val length = LengthModel(
 *     min = 16,
 *     max = 16
 * )
 * ```
 */
data class LengthModel(
    val min: Int? = null,
    val max: Int? = null,
)

/**
 * Defines the security code (CVV) requirements for a card.
 * This class specifies how the security code should be handled,
 * including its location on the card and length requirements.
 *
 * @param mode How the security code should be handled (e.g., "mandatory", "optional")
 * @param location Where the security code is located on the card (e.g., "back", "front")
 * @param length Required length of the security code
 *
 * Example:
 * ```kotlin
 * val securityCode = SecurityCodeModel(
 *     mode = "mandatory",
 *     location = "back",
 *     length = 3
 * )
 * ```
 */
data class SecurityCodeModel(
    val mode: String? = null,
    val location: String? = null,
    val length: Int? = null,
)
