package com.mercadopago.sdk.android.checkout.domain.model

internal data class CardBinData(
    val id: String?,
    val paymentTypeId: String?,
    val cardNumber: CardNumberField?,
    val securityCode: SecurityCodeField?,
    val holderName: CardHolderField?,
    val expirationDate: ExpirationDateField?,
    val issuers: List<BinIssuer>,
    val quotas: List<Quota>,
    val displayType: InstallmentsDisplayType,
    val currencySymbol: String,
    val installmentsTitle: String,
    val installmentsTotalLabel: String,
    val installmentsButtonLabel: String,
)

internal data class BinIssuer(
    val id: String?,
    val name: String?,
)
