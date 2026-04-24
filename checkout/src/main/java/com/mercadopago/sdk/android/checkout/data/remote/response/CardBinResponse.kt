package com.mercadopago.sdk.android.checkout.data.remote.response

internal data class CardBinResponse(
    val paymentMethods: List<PaymentMethodResponse>?,
    val installment: InstallmentConfigResponse?,
    val translations: TranslationsResponse?,
)

internal data class PaymentMethodResponse(
    val id: String?,
    val paymentTypeId: String?,
    val cardNumber: CardNumberConfigResponse?,
    val securityCode: SecurityCodeConfigResponse?,
    val issuers: List<IssuerResponse>?,
)

internal data class CardNumberConfigResponse(
    val length: Int?,
    val validation: String?,
    val mask: String?,
)

internal data class SecurityCodeConfigResponse(
    val mode: String?,
    val length: Int?,
    val cardLocation: String?,
    val tooltip: String? = null,
    val placeholder: String? = null,
)

internal data class IssuerResponse(
    val id: Long?,
    val name: String?,
    val secureThumbnail: String?,
)

internal data class InstallmentConfigResponse(
    val quotas: List<QuotaResponse>?,
)

internal data class QuotaResponse(
    val quantity: Int?,
    val installmentAmount: String?,
    val totalAmount: String?,
    val label: String?,
    val discountRate: Double?,
)

internal data class TranslationsResponse(
    val cardNumber: FieldTranslationResponse?,
    val cardHolderName: FieldTranslationResponse?,
    val expirationDate: FieldTranslationResponse?,
    val securityCode: SecurityCodeTranslationResponse?,
    val identification: FieldTranslationResponse?,
    val installments: InstallmentsTranslationResponse?,
)

internal data class FieldTranslationResponse(
    val label: String?,
    val placeholder: String?,
    val helper: String?,
    val error: FieldErrorTranslationResponse?,
)

internal data class FieldErrorTranslationResponse(
    val invalid: String?,
    val incomplete: String?,
)

internal data class SecurityCodeTranslationResponse(
    val label: String?,
    val placeholder: String?,
    val helper: String?,
    val tooltip: String?,
    val error: FieldErrorTranslationResponse?,
)

internal data class InstallmentsTranslationResponse(
    val label: String?,
    val installmentsSelector: InstallmentsSelectorResponse?,
)

internal data class InstallmentsSelectorResponse(
    val placeholder: String?,
)
