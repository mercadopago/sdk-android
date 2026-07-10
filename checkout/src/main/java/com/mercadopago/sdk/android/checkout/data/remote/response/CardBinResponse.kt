package com.mercadopago.sdk.android.checkout.data.remote.response

import com.google.gson.annotations.SerializedName

internal data class CardBinResponse(
    @SerializedName("payment_methods")
    val paymentMethods: List<PaymentMethodResponse>?,
    @SerializedName("installment")
    val installment: InstallmentConfigResponse?,
    @SerializedName("translations")
    val translations: Translations?,
)

internal data class PaymentMethodResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("payment_type_id")
    val paymentTypeId: String?,
    @SerializedName("card_number")
    val cardNumber: CardNumberConfig?,
    @SerializedName("security_code")
    val securityCode: SecurityCodeConfig?,
    @SerializedName("issuers")
    val issuers: List<IssuerResponse>?,
)

internal data class IssuerResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
)

internal data class InstallmentConfigResponse(
    @SerializedName("selection_type")
    val selectionType: String?,
    @SerializedName("quotas")
    val quotas: List<QuotaResponse>?,
)

internal data class QuotaResponse(
    @SerializedName("installments")
    val installments: Int?,
    @SerializedName("installment_amount")
    val installmentAmount: Float?,
    @SerializedName("total_amount")
    val totalAmount: Float?,
    @SerializedName("primary_label")
    val primaryLabel: String?,
    @SerializedName("secondary_label")
    val secondaryLabel: String?,
    @SerializedName("tertiary_label")
    val tertiaryLabel: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("accessibility_label")
    val accessibilityLabel: String?,
)
