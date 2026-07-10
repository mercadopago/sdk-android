package com.mercadopago.sdk.android.checkout.domain.model

import java.math.BigDecimal

internal data class PaymentBrickInitializationOutput(
    val headerTitle: String,
    val sections: List<PaymentSectionOutput>,
    val footer: PaymentBrickFooterOutput,
)

internal data class PaymentSectionOutput(
    val title: String,
    val methods: List<PaymentMethodOutput>,
)

internal data class PaymentMethodOutput(
    val type: String,
    val title: String,
    val subtitle: String? = null,
    val iconUrl: String? = null,
    val cardData: CardDataOutput? = null,
    val options: List<TicketOptionOutput>? = null,
)

internal data class CardDataOutput(
    val id: String,
    val bin: String,
    val lastFourDigits: String,
    val paymentMethodId: String,
    val paymentTypeId: String,
    val issuerId: Int,
    val securityCode: SecurityCodeOutput,
    val installments: InstallmentsOutput? = null,
)

internal data class SecurityCodeOutput(
    val length: Int,
    val screen: SecurityCodeScreenOutput?,
)

internal data class SecurityCodeScreenOutput(
    val headerTitle: String,
    val field: SecurityCodeFieldOutput,
    val buttonLabel: String,
)

internal data class SecurityCodeFieldOutput(
    val label: String,
    val placeholder: String,
    val helper: String,
)

internal data class InstallmentsOutput(
    val header: InstallmentsHeaderOutput,
    val totalLabel: String,
    val payButtonLabel: String,
    val selectionType: String,
    val quotas: List<QuotaOutput>,
)

internal data class InstallmentsHeaderOutput(
    val title: String,
)

internal data class QuotaOutput(
    val installments: Int,
    val installmentAmount: BigDecimal,
    val totalAmount: BigDecimal,
    val primaryLabel: String,
    val secondaryLabel: String,
    val state: String,
)

internal data class TicketOptionOutput(
    val id: String,
    val name: String,
    val iconUrl: String,
)

internal data class PaymentBrickFooterOutput(
    val totalLabel: String,
    val totalAmount: String,
)
