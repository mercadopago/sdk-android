package com.mercadopago.sdk.android.coremethods.domain.model

data class Installment(
    val paymentMethodId: String? = null,
    val paymentTypeId: String? = null,
    val issuer: Issuer? = null,
    val processingMode: String? = null,
    val merchantAccountId: String? = null,
    val payerCost: List<PayerCost>? = null,
    val agreements: List<Agreements>? = null
)

data class Issuer(
    val id: String?,
    val thumbnail: String?
)

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
    val paymentMethodOptionId: String? = null
)

data class Agreements(
    val merchantAccount: List<MerchantAccount>? = null,
    val timeFrame: TimeFrame? = null
)

data class MerchantAccount(
    val id: String? = null,
    val paymentMethodOptionId: String? = null
)

data class TimeFrame(
    val startDate: String? = null,
    val endDate: String? = null
)
