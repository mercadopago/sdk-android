package com.mercadopago.sdk.android.checkout.data.remote.response

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

internal data class PaymentBrickInitializationResponse(
    @SerializedName("header_title") val headerTitle: String,
    @SerializedName("sections") val sections: List<PaymentSection>,
    @SerializedName("footer") val footer: PaymentBrickFooter,
)

internal data class PaymentSection(
    @SerializedName("title") val title: String,
    @SerializedName("methods") val methods: List<PaymentMethod>,
)

internal data class PaymentMethod(
    @SerializedName("type") val type: String,
    @SerializedName("title") val title: String,
    @SerializedName("subtitle") val subtitle: String? = null,
    @SerializedName("icon_url") val iconUrl: String? = null,
    @SerializedName("card_data") val cardData: CardData? = null,
    @SerializedName("options") val options: List<TicketOption>? = null,
    @SerializedName("screen") val screen: MethodSelectionScreenResponse? = null,
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

internal data class SecurityCode(
    @SerializedName("length") val length: Int,
    @SerializedName("screen") val screen: SecurityCodeScreen? = null,
)

internal data class SecurityCodeScreen(
    @SerializedName("header") val header: SecurityCodeHeader,
    @SerializedName("field") val field: SecurityCodeField,
    @SerializedName("button") val button: SecurityCodeButton,
)

internal data class SecurityCodeHeader(
    @SerializedName("title") val title: String,
)

internal data class SecurityCodeField(
    @SerializedName("label") val label: String,
    @SerializedName("placeholder") val placeholder: String,
    @SerializedName("helper") val helper: String,
    @SerializedName("error") val error: String? = null,
)

internal data class SecurityCodeButton(
    @SerializedName("label") val label: String,
)

internal data class Installments(
    @SerializedName("header") val header: InstallmentsHeader,
    @SerializedName("footer") val footer: InstallmentsFooter,
    @SerializedName("selection_type") val selectionType: String,
    @SerializedName("quotas") val quotas: List<Quota>,
)

internal data class InstallmentsHeader(
    @SerializedName("title") val title: String,
)

internal data class InstallmentsFooter(
    @SerializedName("button") val button: InstallmentsFooterButton,
    @SerializedName("total_label") val totalLabel: String,
    @SerializedName("currency_symbol") val currencySymbol: String,
)

internal data class InstallmentsFooterButton(
    @SerializedName("label") val label: String,
)

internal data class Quota(
    @SerializedName("installments") val installments: Int,
    @SerializedName("installment_amount") val installmentAmount: BigDecimal,
    @SerializedName("total_amount") val totalAmount: BigDecimal,
    @SerializedName("primary_label") val primaryLabel: String,
    @SerializedName("secondary_label") val secondaryLabel: String,
    @SerializedName("tertiary_label") val tertiaryLabel: String? = null,
    @SerializedName("state") val state: String,
    @SerializedName("accessibility_label") val accessibilityLabel: String? = null,
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
