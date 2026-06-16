package com.mercadopago.sdk.android.checkout.data.remote.response

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

/**
 * Response of `GET /cho-off/v1/payment_brick/initialization`.
 *
 * Carries everything the SDK needs to render the payment-method selector screen.
 *
 * Fields are mapped with explicit [SerializedName] so the snake_case contract survives R8
 * minification in release builds — the checkout module ships with `isMinifyEnabled = true`,
 * and `proguard-rules.pro` keeps `@SerializedName` fields. See SMFINTECH-32897.
 *
 * Nullability follows the contract presence rules: fields documented as "Sempre" are
 * non-null; "Opcional"/conditional fields are nullable.
 */
internal data class PaymentBrickInitializationResponse(
    @SerializedName("header_title") val headerTitle: String,
    @SerializedName("sections") val sections: List<PaymentSection>,
    @SerializedName("footer") val footer: PaymentBrickFooter,
)

internal data class PaymentSection(
    @SerializedName("title") val title: String,
    @SerializedName("methods") val methods: List<PaymentMethod>,
)

/**
 * A single payment option inside a section.
 *
 * [type] is `saved_card` | `new_card` | `ticket` | `wallet` | `credits`. The optional
 * blocks are mutually exclusive by type: [cardData] only for `saved_card`, [options] only
 * for `ticket`; `new_card` carries neither.
 */
internal data class PaymentMethod(
    @SerializedName("type") val type: String,
    @SerializedName("title") val title: String,
    @SerializedName("subtitle") val subtitle: String? = null,
    @SerializedName("icon_url") val iconUrl: String? = null,
    @SerializedName("card_data") val cardData: CardData? = null,
    @SerializedName("options") val options: List<TicketOption>? = null,
)

internal data class CardData(
    @SerializedName("id") val id: String,
    @SerializedName("bin") val bin: String,
    @SerializedName("last_four_digits") val lastFourDigits: String,
    @SerializedName("payment_method_id") val paymentMethodId: String,
    @SerializedName("payment_type_id") val paymentTypeId: String,
    @SerializedName("issuer_id") val issuerId: Int,
    @SerializedName("security_code") val securityCode: SecurityCode,
    @SerializedName("installments") val installments: Installments? = null,
)

/**
 * CVV configuration for a saved card.
 *
 * [screen] present = SDK shows the CVV screen; absent = SDK skips it
 * (`has_preapproval_scope=true` or `length=0`).
 */
internal data class SecurityCode(
    @SerializedName("length") val length: Int,
    @SerializedName("screen") val screen: SecurityCodeScreen? = null,
)

internal data class SecurityCodeScreen(
    @SerializedName("header_title") val headerTitle: String,
    @SerializedName("field") val field: SecurityCodeField,
    @SerializedName("continue_button_label") val continueButtonLabel: String,
)

internal data class SecurityCodeField(
    @SerializedName("label") val label: String,
    @SerializedName("placeholder") val placeholder: String,
    @SerializedName("helper") val helper: String,
)

internal data class Installments(
    @SerializedName("header") val header: InstallmentsHeader,
    @SerializedName("total_label") val totalLabel: String,
    @SerializedName("pay_button_label") val payButtonLabel: String,
    @SerializedName("selection_type") val selectionType: String,
    @SerializedName("quotas") val quotas: List<Quota>,
)

internal data class InstallmentsHeader(
    @SerializedName("title") val title: String,
)

internal data class Quota(
    @SerializedName("installments") val installments: Int,
    @SerializedName("installment_amount") val installmentAmount: BigDecimal,
    @SerializedName("total_amount") val totalAmount: BigDecimal,
    @SerializedName("primary_label") val primaryLabel: String,
    @SerializedName("secondary_label") val secondaryLabel: String,
    @SerializedName("state") val state: String,
)

internal data class TicketOption(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("icon_url") val iconUrl: String,
)

internal data class PaymentBrickFooter(
    @SerializedName("total_label") val totalLabel: String,
    @SerializedName("total_amount") val totalAmount: String,
)
