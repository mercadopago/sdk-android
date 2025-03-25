package com.mercadopago.sdk.android.coremethods.domain.model

/**
 * Installment contains details related to one or more installments.
 * @param paymentMethodId: The ID of the payment method.
 * @param paymentTypeId: The ID of the payment type.
 * @param issuer: The issuer of the payment method.
 * @param processingMode: The processing mode of the payment method.
 * @param merchantAccountId: The ID of the merchant account.
 * @param payerCost: The list of payer costs.
 * @param agreements: The list of agreements.
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
 * Issuer contains details related to the issuer of the payment method.
 * @param id: The ID of the issuer.
 * @param thumbnail: The thumbnail of the issuer.
 */
data class Issuer(
    val id: String? = null,
    val thumbnail: String? = null,
)

/**
 * PayerCost contains details related to the payer cost.
 * @param instalments: The number of instalments.
 * @param installmentAmount: The amount of the installment.
 * @param instalmentsRate: The rate of the instalments.
 * @param installmentRateCollector: The collector of the instalments rate.
 * @param totalAmount: The total amount.
 * @param minAllowedAmount: The minimum allowed amount.
 * @param maxAllowedAmount: The maximum allowed amount.
 * @param discountRate: The discount rate.
 * @param reimbursementRate: The reimbursement rate.
 * @param labels: The list of labels.
 * @param paymentMethodOptionId: The ID of the payment method option.
 */
data class PayerCost(
    val instalments: Int? = null,
    val installmentAmount: Int? = null,
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
 * Agreements contains details related to the agreements.
 * @param merchantAccount: The list of merchant accounts.
 * @param timeFrame: The time frame.
 */
data class Agreements(
    val merchantAccount: List<MerchantAccount>? = null,
    val timeFrame: TimeFrame? = null,
)

/**
 * MerchantAccount contains details related to the merchant account.
 * @param id: The ID of the merchant account.
 * @param paymentMethodOptionId: The ID of the payment method option.
 */
data class MerchantAccount(
    val id: String? = null,
    val paymentMethodOptionId: String? = null,
)

/**
 * TimeFrame contains details related to the time frame.
 * @param startDate: The start date.
 * @param endDate: The end date.
 */
data class TimeFrame(
    val startDate: String? = null,
    val endDate: String? = null,
)

/**
 * ProcessingMode contains the processing mode of the payment method.
 * @param mode: The processing mode.
 */
enum class ProcessingMode(val mode: String) {
    /**
     * Aggregator processing mode.
     */
    Aggregator("aggregator"),

    /**
     * Gateway processing mode.
     */
    Gateway("gateway"),
}
