package com.mercadopago.sdk.android.checkout.domain.model

internal data class CardBinData(
    val id: String?,
    val paymentTypeId: String?,
    val cardNumber: CardNumberConfig?,
    val securityCode: BinSecurityCodeConfig?,
    val issuers: List<BinIssuer>,
    val quotas: List<Quota>,
    val translations: CardFormTranslations?,
)

internal data class CardNumberConfig(
    val length: Int?,
    val validation: String?,
    val mask: String?,
)

internal data class BinSecurityCodeConfig(
    val mode: String?,
    val length: Int?,
    val cardLocation: String?,
)

internal data class BinIssuer(
    val id: Long?,
    val name: String?,
    val secureThumbnail: String?,
)

internal data class Quota(
    val quantity: Int?,
    val installmentAmount: String?,
    val totalAmount: String?,
    val label: String?,
    val discountRate: Double?,
)

internal data class CardFormTranslations(
    val cardNumber: FieldTranslation?,
    val cardHolderName: FieldTranslation?,
    val expirationDate: FieldTranslation?,
    val securityCode: SecurityCodeFieldTranslation?,
    val identification: FieldTranslation?,
    val installments: InstallmentsFieldTranslation?,
)

internal data class FieldTranslation(
    val label: String?,
    val placeholder: String?,
    val helper: String?,
    val error: FieldErrorTranslation?,
)

internal data class FieldErrorTranslation(
    val invalid: String?,
    val incomplete: String?,
)

internal data class SecurityCodeFieldTranslation(
    val label: String?,
    val placeholder: String?,
    val helper: String?,
    val tooltip: String?,
    val error: FieldErrorTranslation?,
)

internal data class InstallmentsFieldTranslation(
    val label: String?,
    val installmentsSelectorPlaceholder: String?,
)
