package com.mercadopago.sdk.android.coremethods.data.remote.response

import com.google.gson.annotations.SerializedName

internal data class InstallmentsResponse(
    @SerializedName("payment_method_id")
    val paymentMethodId: String? = null,
    @SerializedName("payment_type_id")
    val paymentTypeId: String? = null,
    @SerializedName("issuer")
    val issuer: IssuerResponse? = null,
    @SerializedName("processing_mode")
    val processingMode: String? = null,
    @SerializedName("merchant_account_id")
    val merchantAccountId: String? = null,
    @SerializedName("payer_costs")
    val payerCost: List<PayerCostResponse>? = null,
    @SerializedName("agreements")
    val agreements: List<AgreementsResponse>? = null,
)

internal data class IssuerResponse(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("thumbnail")
    val thumbnail: String? = null,
)

internal data class PayerCostResponse(
    @SerializedName("installments")
    val instalments: Int? = null,
    @SerializedName("installment_amount")
    val installmentAmount: Int? = null,
    @SerializedName("installment_rate")
    val instalmentsRate: Float? = null,
    @SerializedName("installment_rate_collector")
    val installmentRateCollector: List<String>? = null,
    @SerializedName("total_amount")
    val totalAmount: Float? = null,
    @SerializedName("min_allowed_amount")
    val minAllowedAmount: Float? = null,
    @SerializedName("max_allowed_amount")
    val maxAllowedAmount: Float? = null,
    @SerializedName("discount_rate")
    val discountRate: Float? = null,
    @SerializedName("reimbursement_rate")
    val reimbursementRate: Float? = null,
    @SerializedName("labels")
    val labels: List<String>? = null,
    @SerializedName("payment_method_option_id")
    val paymentMethodOptionId: String? = null,
)

internal data class AgreementsResponse(
    @SerializedName("merchant_accounts")
    val merchantAccount: List<MerchantAccountResponse>? = null,
    @SerializedName("time_frame")
    val timeFrame: TimeFrameResponse? = null,
)

internal data class MerchantAccountResponse(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("payment_method_option_id")
    val paymentMethodOptionId: String? = null,
)

internal data class TimeFrameResponse(
    @SerializedName("start_date")
    val startDate: String? = null,
    @SerializedName("end_date")
    val endDate: String? = null,
)
