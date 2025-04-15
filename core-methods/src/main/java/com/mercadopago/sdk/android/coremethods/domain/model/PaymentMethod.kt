package com.mercadopago.sdk.android.coremethods.domain.model

/**
 * Represents a payment method with various details related to financial transactions.
 *
 * @param financialInstitution A list of financial institutions associated with the payment method.
 * @param payerCost A list of costs associated with the payer for this payment method.
 * @param issuer The issuer of the payment method, which may be an entity or financial institution.
 * @param totalFinancialCost The total cost incurred for using the payment method, as a string.
 * @param minAccreditationDays The minimum number of days required for accreditation, as a string.
 * @param maxAccreditationDays The maximum number of days required for accreditation, as a string.
 * @param merchantAccountId An identifier for the merchant account associated with this payment method, as a string.
 * @param id A unique identifier for this payment method.
 * @param paymentTypeId The type of payment associated with this method.
 * @param accreditationTime The time required for accreditation, as a string.
 * @param card The card details associated with the payment method, if applicable.
 * @param thumbnail A thumbnail image URL representing the payment method.
 * @param bins A list of bank identification numbers (BINs) associated with this payment method.
 * @param marketplace An identifier for the marketplace this payment method is available in.
 * @param deferredCapture Indicates if the capture of money is deferred.
 * @param agreements A list of agreements applicable to this payment method.
 * @param labels A list of labels that provide additional information about the payment method.
 * @param siteId The identifier for the site in which this payment method is used.
 * @param processingMode The mode of processing for the payment.
 * @param additionalInfoNeeded A list of additional information required for processing this payment method.
 * @param status The current status of the payment method.
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
    val status: String? = null
)

/**
 * Represents a financial institution involved in processing the payment method.
 *
 * @param id A unique identifier for the financial institution.
 * @param description A description of the financial institution.
 */
data class FinancialInstitutionModel(
    val id: String?,
    val description: String
)

/**
 * Represents the card details associated with a payment method.
 *
 * @param bin The Bank Identification Number associated with the card.
 * @param length The length specifications of the card number.
 * @param validation A validation method for the card.
 * @param securityCode The security code details for the card.
 */
data class CardModel(
    val bin: Int? = null,
    val length: LengthModel? = null,
    val validation: String? = null,
    val securityCode: SecurityCodeModel? = null
)

/**
 * Represents the length specifications of a card number.
 *
 * @param min The minimum length of the card number.
 * @param max The maximum length of the card number.
 */
data class LengthModel(
    val min: Int? = null,
    val max: Int? = null
)

/**
 * Represents the security code details for a card.
 *
 * @param mode The mode in which the security code is used (e.g., presence, absence).
 * @param location The location of the security code on the card.
 * @param length The length of the security code.
 */
data class SecurityCodeModel(
    val mode: String? = null,
    val location: String? = null,
    val length: Int? = null
)
