package com.mercadopago.sdk.android.checkout.data.remote.response

internal data class CardBinResponse(
    val paymentMethods: List<PaymentMethodResponse>?,
    val installment: InstallmentConfigResponse?,
    val translations: Translations?,
)

internal data class PaymentMethodResponse(
    val id: String?,
    val paymentTypeId: String?,
    val cardNumber: CardNumberConfig?,
    val securityCode: SecurityCodeConfig?,
    val issuers: List<IssuerResponse>?,
)

internal data class IssuerResponse(
    val id: Long?,
    val name: String?,
    val secureThumbnail: String?,
)

internal data class InstallmentConfigResponse(
    val selectionType: String?,
    val quotas: List<QuotaResponse>?,
)

internal data class QuotaResponse(
    val installments: Int?,
    val installmentAmount: Float?,
    val totalAmount: Float?,
    val primaryLabel: String? = null,
    val secondaryLabel: String? = null,
    val tertiaryLabel: String? = null,
    val state: String? = null,
)
