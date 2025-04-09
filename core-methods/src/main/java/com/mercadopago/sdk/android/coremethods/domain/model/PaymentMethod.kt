package com.mercadopago.sdk.android.coremethods.domain.model

data class PaymentMethod(
    val financialInstitution: FinancialInstitutionModel? = null,
    val payerCost: List<PayerCost>? = null,
    val issuer: Issuer? = null,
    val totalFinancialCost: String? = null,
    val minAccreditationDays: String? = null,
    val maxAccreditationDays: String? = null,
    val merchantAccountId: String? = null,
    val id: String? = null,
    val paymentTypeId: String? = null,
    val accreditationTime: String? = null,
    val card: CardModel? = null,
    val thumbnail: String? = null,
    val bins: List<Int>? = null,
    val marketplace: String? = null,
    val deferredCapture: String? = null,
    val agreements: List<Agreements>? = null,
    val labels: List<String>? = null,
    val siteId: String? = null,
    val processingMode: String? = null,
    val additionalInfoNeeded: String? = null,
    val status: String? = null
)

data class FinancialInstitutionModel(
    val id: String?,
    val description: String
)

data class CardModel(
    val bin: Int? = null,
    val length: LengthModel? = null,
    val validation: String? = null,
    val securityCode: SecurityCodeModel? = null
)

data class LengthModel(
    val min: Int? = null,
    val max: Int? = null
)

data class SecurityCodeModel(
    val mode: String? = null,
    val location: String? = null,
    val length: Int? = null
)
