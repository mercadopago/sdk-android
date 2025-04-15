package com.mercadopago.sdk.android.coremethods.data.remote.response

import com.google.gson.annotations.SerializedName

internal data class PaymentMethodResponse(
    @SerializedName("financial_institutions")
    val financialInstitution: List<FinancialInstitutionResponse>? = null,
    @SerializedName("payerCost")
    val payerCost: List<PayerCostResponse>? = null,
    @SerializedName("issuer")
    val issuer: IssuerResponse? = null,
    @SerializedName("total_financial_cost")
    val totalFinancialCost: String? = null,
    @SerializedName("min_accreditation_days")
    val minAccreditationDays: String? = null,
    @SerializedName("max_accreditation_days")
    val maxAccreditationDays: String? = null,
    @SerializedName("merchant_account_id")
    val merchantAccountId: String? = null,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("payment_type_id")
    val paymentTypeId: String? = null,
    @SerializedName("accreditation_time")
    val accreditationTime: String? = null,
    @SerializedName("card")
    val card: CardResponse? = null,
    @SerializedName("thumbnail")
    val thumbnail: String? = null,
    @SerializedName("bins")
    val bins: List<Int>? = null,
    @SerializedName("marketplace")
    val marketplace: String? = null,
    @SerializedName("deferred_capture")
    val deferredCapture: String? = null,
    @SerializedName("agreements")
    val agreements: List<AgreementsResponse>? = null,
    @SerializedName("labels")
    val labels: List<String>? = null,
    @SerializedName("site_id")
    val siteId: String? = null,
    @SerializedName("processing_mode")
    val processingMode: String? = null,
    @SerializedName("additional_info_needed")
    val additionalInfoNeeded: List<String>? = null,
    @SerializedName("status")
    val status: String? = null
)

internal data class FinancialInstitutionResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("description")
    val description: String
)

internal data class CardResponse(
    @SerializedName("bin")
    val bin: Int? = null,
    @SerializedName("length")
    val length: LengthResponse? = null,
    @SerializedName("validation")
    val validation: String? = null,
    @SerializedName("security_code")
    val securityCode: SecurityCodeResponse? = null,
)

internal data class LengthResponse(
    @SerializedName("min")
    val min: Int? = null,
    @SerializedName("max")
    val max: Int? = null
)

internal data class SecurityCodeResponse(
    @SerializedName("mode")
    val mode: String? = null,
    @SerializedName("location")
    val location: String? = null,
    @SerializedName("length")
    val length: Int? = null
)
