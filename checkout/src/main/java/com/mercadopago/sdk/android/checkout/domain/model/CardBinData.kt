package com.mercadopago.sdk.android.checkout.domain.model

import com.mercadopago.sdk.android.checkout.data.remote.response.FieldTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeTranslations

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
    val tooltip: String? = null,
    val placeholder: String? = null,
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
    val cardNumber: FieldTranslations?,
    val cardHolderName: FieldTranslations?,
    val expirationDate: FieldTranslations?,
    val securityCode: SecurityCodeTranslations?,
)
