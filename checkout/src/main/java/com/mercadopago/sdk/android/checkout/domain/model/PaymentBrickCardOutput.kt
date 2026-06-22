package com.mercadopago.sdk.android.checkout.domain.model

import java.math.BigDecimal

internal data class PaymentBrickCardOutput(
    val translations: CardTranslationsOutput,
    val installment: InstallmentConfigOutput?,
    val paymentMethods: List<PaymentMethodConfigOutput>,
)

internal data class CardTranslationsOutput(
    val cardFormTitle: String,
    val cardFormFooterButtonLabel: String,
    val cardNumber: CardFieldTranslationsOutput,
    val securityCode: CardSecurityCodeTranslationsOutput,
    val expirationDate: CardFieldTranslationsOutput,
    val holderName: CardHolderNameTranslationsOutput,
    val installments: CardInstallmentsTranslationsOutput,
)

internal data class CardFieldTranslationsOutput(
    val label: String,
    val placeholder: String,
    val errorEmptyField: String,
    val errorIncompleteField: String,
    val errorInvalidField: String?,
    val helper: String?,
)

internal data class CardHolderNameTranslationsOutput(
    val label: String,
    val placeholder: String,
    val helper: String?,
)

internal data class CardSecurityCodeTranslationsOutput(
    val label: String,
    val placeholder: String,
    val tooltip: String?,
    val errorEmptyField: String,
    val errorIncompleteField: String,
)

internal data class CardInstallmentsTranslationsOutput(
    val header: CardInstallmentsHeaderOutput,
    val interestFreeLabel: String,
    val totalLabel: String,
)

internal data class CardInstallmentsHeaderOutput(
    val title: String,
)

internal data class InstallmentConfigOutput(
    val selectionType: String,
    val quotas: List<CardQuotaOutput>,
)

internal data class CardQuotaOutput(
    val installments: Int,
    val installmentAmount: BigDecimal,
    val totalAmount: BigDecimal,
    val primaryLabel: String,
    val secondaryLabel: String,
    val state: String,
    val accessibilityLabel: String?,
)

internal data class PaymentMethodConfigOutput(
    val id: String,
    val paymentTypeId: String,
    val cardNumber: CardNumberFieldConfigOutput?,
    val securityCode: SecurityCodeFieldConfigOutput?,
    val issuers: List<CardIssuerConfigOutput>?,
)

internal data class CardNumberFieldConfigOutput(
    val type: String,
    val minLength: Int,
    val maxLength: Int,
    val mask: String?,
)

internal data class SecurityCodeFieldConfigOutput(
    val mode: String,
    val length: Int,
    val placeholder: String?,
)

internal data class CardIssuerConfigOutput(
    val id: String,
    val name: String,
)
