package com.mercadopago.sdk.android.checkout.data.remote.response

import com.google.gson.annotations.SerializedName

internal data class CardBinResponse(
    @SerializedName("id") val id: String?,
    @SerializedName("payment_type_id") val paymentTypeId: String?,
    @SerializedName("card_number") val cardNumber: CardNumberConfigResponse?,
    @SerializedName("security_code") val securityCode: SecurityCodeConfigResponse?,
    @SerializedName("issuers") val issuers: List<IssuerResponse>?,
    @SerializedName("installment") val installment: InstallmentConfigResponse?,
    @SerializedName("translations") val translations: TranslationsResponse?,
)

internal data class CardNumberConfigResponse(
    @SerializedName("length") val length: Int?,
    @SerializedName("validation") val validation: String?,
    @SerializedName("mask") val mask: String?,
)

internal data class SecurityCodeConfigResponse(
    @SerializedName("mode") val mode: String?,
    @SerializedName("length") val length: Int?,
    @SerializedName("card_location") val cardLocation: String?,
)

internal data class IssuerResponse(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("secure_thumbnail") val secureThumbnail: String?,
)

internal data class InstallmentConfigResponse(
    @SerializedName("quotas") val quotas: List<QuotaResponse>?,
)

internal data class QuotaResponse(
    @SerializedName("quantity") val quantity: Int?,
    @SerializedName("installment_amount") val installmentAmount: String?,
    @SerializedName("total_amount") val totalAmount: String?,
    @SerializedName("label") val label: String?,
    @SerializedName("discount_rate") val discountRate: Double?,
)

internal data class TranslationsResponse(
    @SerializedName("card_number") val cardNumber: FieldTranslationResponse?,
    @SerializedName("card_holder_name") val cardHolderName: FieldTranslationResponse?,
    @SerializedName("expiration_date") val expirationDate: FieldTranslationResponse?,
    @SerializedName("security_code") val securityCode: SecurityCodeTranslationResponse?,
    @SerializedName("identification") val identification: FieldTranslationResponse?,
    @SerializedName("installments") val installments: InstallmentsTranslationResponse?,
)

internal data class FieldTranslationResponse(
    @SerializedName("label") val label: String?,
    @SerializedName("placeholder") val placeholder: String?,
    @SerializedName("helper") val helper: String?,
    @SerializedName("error") val error: FieldErrorTranslationResponse?,
)

internal data class FieldErrorTranslationResponse(
    @SerializedName("invalid") val invalid: String?,
    @SerializedName("incomplete") val incomplete: String?,
)

internal data class SecurityCodeTranslationResponse(
    @SerializedName("label") val label: String?,
    @SerializedName("placeholder") val placeholder: String?,
    @SerializedName("helper") val helper: String?,
    @SerializedName("tooltip") val tooltip: String?,
    @SerializedName("error") val error: FieldErrorTranslationResponse?,
)

internal data class InstallmentsTranslationResponse(
    @SerializedName("label") val label: String?,
    @SerializedName("installments_selector") val installmentsSelector: InstallmentsSelectorResponse?,
)

internal data class InstallmentsSelectorResponse(
    @SerializedName("placeholder") val placeholder: String?,
)
