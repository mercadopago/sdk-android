package com.mercadopago.sdk.android.checkout.data.remote.response

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

/**
 * Response of `GET /cho-off/v1/payment_brick/card`.
 *
 * Returns card form configuration when the buyer selects "Nueva tarjeta" (new card).
 * Same shape as `GET /card_payment_brick/card` — BFF delegates internally to the same service.
 *
 * Fields use explicit [SerializedName] to survive R8 minification (same rationale as
 * [PaymentBrickInitializationResponse]). See SMFINTECH-32897.
 */
internal data class PaymentBrickCardResponse(
    @SerializedName("translations") val translations: CardTranslations,
    @SerializedName("installment") val installment: InstallmentConfig?,
    @SerializedName("payment_methods") val paymentMethods: List<PaymentMethodConfig>,
)

internal data class CardTranslations(
    @SerializedName("card_form_title") val cardFormTitle: String,
    @SerializedName("card_form_footer_button_label") val cardFormFooterButtonLabel: String,
    @SerializedName("card_number") val cardNumber: CardFieldTranslations,
    @SerializedName("security_code") val securityCode: CardSecurityCodeTranslations,
    @SerializedName("expiration_date") val expirationDate: CardFieldTranslations,
    @SerializedName("holder_name") val holderName: CardHolderNameTranslations,
    @SerializedName("installments") val installments: CardInstallmentsTranslations,
)

internal data class CardFieldTranslations(
    @SerializedName("label") val label: String,
    @SerializedName("placeholder") val placeholder: String,
    @SerializedName("error_empty_field") val errorEmptyField: String,
    @SerializedName("error_incomplete_field") val errorIncompleteField: String,
    @SerializedName("error_invalid_field") val errorInvalidField: String? = null,
    @SerializedName("helper") val helper: String? = null,
)

internal data class CardHolderNameTranslations(
    @SerializedName("label") val label: String,
    @SerializedName("placeholder") val placeholder: String,
    @SerializedName("helper") val helper: String? = null,
)

internal data class CardSecurityCodeTranslations(
    @SerializedName("label") val label: String,
    @SerializedName("placeholder") val placeholder: String,
    @SerializedName("tooltip") val tooltip: String? = null,
    @SerializedName("error_empty_field") val errorEmptyField: String,
    @SerializedName("error_incomplete_field") val errorIncompleteField: String,
)

internal data class CardInstallmentsTranslations(
    @SerializedName("header") val header: CardInstallmentsHeaderTranslations,
    @SerializedName("interest_free_label") val interestFreeLabel: String,
    @SerializedName("total_label") val totalLabel: String,
)

internal data class CardInstallmentsHeaderTranslations(
    @SerializedName("chevron") val chevron: String,
    @SerializedName("radio") val radio: String,
    @SerializedName("title") val title: String,
)

internal data class InstallmentConfig(
    @SerializedName("selection_type") val selectionType: String,
    @SerializedName("quotas") val quotas: List<CardQuota>,
)

internal data class CardQuota(
    @SerializedName("installments") val installments: Int,
    @SerializedName("installment_amount") val installmentAmount: BigDecimal,
    @SerializedName("total_amount") val totalAmount: BigDecimal,
    @SerializedName("primary_label") val primaryLabel: String,
    @SerializedName("secondary_label") val secondaryLabel: String,
    @SerializedName("state") val state: String,
    @SerializedName("accessibility_label") val accessibilityLabel: String? = null,
)

internal data class PaymentMethodConfig(
    @SerializedName("id") val id: String,
    @SerializedName("payment_type_id") val paymentTypeId: String,
    @SerializedName("card_number") val cardNumber: CardNumberFieldConfig? = null,
    @SerializedName("security_code") val securityCode: SecurityCodeFieldConfig? = null,
    @SerializedName("issuers") val issuers: List<CardIssuerConfig>? = null,
)

internal data class CardNumberFieldConfig(
    @SerializedName("type") val type: String,
    @SerializedName("length") val length: CardFieldLength,
    @SerializedName("mask") val mask: String? = null,
)

internal data class CardFieldLength(
    @SerializedName("min") val min: Int,
    @SerializedName("max") val max: Int,
)

internal data class SecurityCodeFieldConfig(
    @SerializedName("mode") val mode: String,
    @SerializedName("length") val length: Int,
    @SerializedName("type") val type: String? = null,
    @SerializedName("tooltip") val tooltip: String? = null,
    @SerializedName("placeholder") val placeholder: String? = null,
)

internal data class CardIssuerConfig(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
)
