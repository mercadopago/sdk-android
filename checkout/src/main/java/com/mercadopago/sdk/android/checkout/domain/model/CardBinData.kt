package com.mercadopago.sdk.android.checkout.domain.model

import com.mercadopago.sdk.android.checkout.data.remote.response.CardNumberConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.Translations

internal data class CardBinData(
    val id: String?,
    val paymentTypeId: String?,
    val cardNumber: CardNumberConfig?,
    val securityCode: SecurityCodeConfig?,
    val issuers: List<BinIssuer>,
    val quotas: List<Quota>,
    val installmentsSelectionType: String?,
    val translations: Translations?,
)

internal data class BinIssuer(
    val id: Long?,
    val name: String?,
    val secureThumbnail: String?,
)
