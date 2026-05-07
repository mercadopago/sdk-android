package com.mercadopago.sdk.android.checkout.data.remote.mapper

import com.mercadopago.sdk.android.checkout.data.remote.response.CardBinResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.IssuerResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.QuotaResponse
import com.mercadopago.sdk.android.checkout.domain.model.BinIssuer
import com.mercadopago.sdk.android.checkout.domain.model.CardBinData
import com.mercadopago.sdk.android.coremethods.domain.model.PayerCost

internal fun CardBinResponse.toDomain(): CardBinData {
    val paymentMethod = paymentMethods?.firstOrNull()
    return CardBinData(
        id = paymentMethod?.id,
        paymentTypeId = paymentMethod?.paymentTypeId,
        cardNumber = paymentMethod?.cardNumber,
        securityCode = paymentMethod?.securityCode,
        issuers = paymentMethod?.issuers?.map { it.toDomain() } ?: emptyList(),
        payerCosts = installment?.quotas?.map { it.toDomain() } ?: emptyList(),
        installmentsSelectionType = installment?.selectionType,
        translations = translations,
    )
}

private fun IssuerResponse.toDomain(): BinIssuer =
    BinIssuer(
        id = id,
        name = name,
        secureThumbnail = secureThumbnail,
    )

private fun QuotaResponse.toDomain(): PayerCost =
    PayerCost(
        instalments = installments,
        installmentAmount = installmentAmount?.toBigDecimal(),
        totalAmount = totalAmount?.toBigDecimal(),
    )
