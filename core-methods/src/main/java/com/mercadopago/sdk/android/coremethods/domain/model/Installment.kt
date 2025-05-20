package com.mercadopago.sdk.android.coremethods.domain.model

/**
 * Represents installment options and payment details for a specific payment method.
 * This class contains comprehensive information about available installment plans,
 * including costs, rates, and processing details for a payment method.
 * It helps merchants and users understand the total cost and payment options.
 *
 * @param paymentMethodId Unique identifier for the payment method (e.g., "visa", "master")
 * @param paymentTypeId Type of payment (e.g., "credit_card", "debit_card")
 * @param issuer Information about the card issuer or financial institution
 * @param processingMode Mode of payment processing (e.g., "aggregator", "gateway")
 * @param merchantAccountId Unique identifier for the merchant's account
 * @param payerCost List of available installment options with their costs
 * @param agreements List of agreements and terms for the payment method
 *
 * Example:
 * ```kotlin
 * val installment = Installment(
 *     paymentMethodId = "visa",
 *     paymentTypeId = "credit_card",
 *     issuer = Issuer(
 *         id = "123",
 *         thumbnail = "https://example.com/visa.png"
 *     ),
 *     payerCost = listOf(
 *         PayerCost(
 *             instalments = 1,
 *             installmentAmount = 100.0f,
 *             totalAmount = 100.0f
 *         )
 *     )
 * )
 * ```
 *
 * @see Issuer
 * @see PayerCost
 * @see Agreements
 * @see ProcessingMode
 */
data class Installment(
    val paymentMethodId: String? = null,
    val paymentTypeId: String? = null,
    val issuer: Issuer? = null,
    val processingMode: String? = null,
    val merchantAccountId: String? = null,
    val payerCost: List<PayerCost>? = null,
    val agreements: List<Agreements>? = null,
)

/**
 * Represents information about the card issuer or financial institution.
 * This class contains details about the entity that issues the payment method,
 * including its identification and visual representation.
 *
 * @param id Unique identifier for the issuer
 * @param thumbnail URL to the issuer's logo or icon
 * @param default Indicates if this is the default issuer for the payment method
 *
 * Example:
 * ```kotlin
 * val issuer = Issuer(
 *     id = "123",
 *     thumbnail = "https://example.com/visa.png",
 *     default = true
 * )
 * ```
 */
data class Issuer(
    val id: String? = null,
    val thumbnail: String? = null,
    val default: Boolean? = null,
)

/**
 * Represents detailed cost information for an installment plan.
 * This class contains all financial details related to an installment option,
 * including rates, amounts, and payment terms.
 *
 * @param instalments Number of installments in this plan
 * @param installmentAmount Amount to be paid in each installment
 * @param instalmentsRate Interest rate applied to the installments
 * @param installmentRateCollector List of entities collecting the installment rates
 * @param totalAmount Total amount to be paid including all fees
 * @param minAllowedAmount Minimum amount allowed for this installment plan
 * @param maxAllowedAmount Maximum amount allowed for this installment plan
 * @param discountRate Discount rate applied to the total amount
 * @param reimbursementRate Rate for reimbursement if applicable
 * @param labels Additional labels or tags for this installment plan
 * @param paymentMethodOptionId Unique identifier for this payment option
 *
 * Example:
 * ```kotlin
 * val payerCost = PayerCost(
 *     instalments = 3,
 *     installmentAmount = 33.33f,
 *     totalAmount = 100.0f,
 *     instalmentsRate = 0.0f,
 *     labels = listOf("interest_free")
 * )
 * ```
 */
data class PayerCost(
    val instalments: Int? = null,
    val installmentAmount: Float? = null,
    val instalmentsRate: Float? = null,
    val installmentRateCollector: List<String>? = null,
    val totalAmount: Float? = null,
    val minAllowedAmount: Float? = null,
    val maxAllowedAmount: Float? = null,
    val discountRate: Float? = null,
    val reimbursementRate: Float? = null,
    val labels: List<String>? = null,
    val paymentMethodOptionId: String? = null,
)

/**
 * Represents agreements and terms for a payment method.
 * This class contains information about merchant accounts and time frames
 * associated with the payment method's agreements.
 *
 * @param merchantAccount List of merchant accounts associated with the agreement
 * @param timeFrame Time period during which the agreement is valid
 *
 * Example:
 * ```kotlin
 * val agreements = Agreements(
 *     merchantAccount = listOf(
 *         MerchantAccount(id = "123", paymentMethodOptionId = "visa")
 *     ),
 *     timeFrame = TimeFrame(
 *         startDate = "2024-01-01",
 *         endDate = "2024-12-31"
 *     )
 * )
 * ```
 *
 * @see MerchantAccount
 * @see TimeFrame
 */
data class Agreements(
    val merchantAccount: List<MerchantAccount>? = null,
    val timeFrame: TimeFrame? = null,
)

/**
 * Represents a merchant account associated with a payment method.
 * This class contains identification information for a merchant's
 * payment processing account.
 *
 * @param id Unique identifier for the merchant account
 * @param paymentMethodOptionId Identifier for the payment method option
 *
 * Example:
 * ```kotlin
 * val merchantAccount = MerchantAccount(
 *     id = "123",
 *     paymentMethodOptionId = "visa"
 * )
 * ```
 */
data class MerchantAccount(
    val id: String? = null,
    val paymentMethodOptionId: String? = null,
)

/**
 * Represents a time period for an agreement or promotion.
 * This class defines the validity period for payment method agreements,
 * promotions, or special conditions.
 *
 * @param startDate Start date of the time period (ISO format)
 * @param endDate End date of the time period (ISO format)
 *
 * Example:
 * ```kotlin
 * val timeFrame = TimeFrame(
 *     startDate = "2024-01-01",
 *     endDate = "2024-12-31"
 * )
 * ```
 */
data class TimeFrame(
    val startDate: String? = null,
    val endDate: String? = null,
)

/**
 * Defines the processing modes available for payment methods.
 * This enum represents different ways in which payments can be processed,
 * affecting how transactions are handled and settled.
 *
 * @param mode String representation of the processing mode
 *
 * Example:
 * ```kotlin
 * val processingMode = ProcessingMode.Aggregator
 * // or
 * val processingMode = ProcessingMode.Gateway
 * ```
 */
enum class ProcessingMode(val mode: String) {
    /**
     * Aggregator processing mode.
     * In this mode, the payment processor aggregates transactions
     * from multiple merchants before processing them.
     */
    Aggregator("aggregator"),

    /**
     * Gateway processing mode.
     * In this mode, the payment processor acts as a direct gateway
     * between the merchant and the payment network.
     */
    Gateway("gateway"),
}
