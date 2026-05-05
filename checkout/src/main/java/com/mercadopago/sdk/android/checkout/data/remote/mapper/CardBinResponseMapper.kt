package com.mercadopago.sdk.android.checkout.data.remote.mapper

import com.mercadopago.sdk.android.checkout.data.remote.response.CardBinResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.IssuerResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.QuotaResponse
import com.mercadopago.sdk.android.checkout.domain.model.BinIssuer
import com.mercadopago.sdk.android.checkout.domain.model.CardBinData
import com.mercadopago.sdk.android.checkout.domain.model.Quota

internal fun CardBinResponse.toDomain(): CardBinData {
    val paymentMethod = paymentMethods?.firstOrNull()
    return CardBinData(
        id = paymentMethod?.id,
        paymentTypeId = paymentMethod?.paymentTypeId,
        cardNumber = paymentMethod?.cardNumber,
        securityCode = paymentMethod?.securityCode,
        issuers = paymentMethod?.issuers?.map { it.toDomain() } ?: emptyList(),
        quotas = installment?.quotas?.map { it.toDomain() } ?: emptyList(),
        translations = translations,
    )
}

private fun IssuerResponse.toDomain(): BinIssuer =
    BinIssuer(
        id = id,
        name = name,
        secureThumbnail = secureThumbnail,
    )

private fun QuotaResponse.toDomain(): Quota =
    Quota(
        quantity = quantity,
        installmentAmount = installmentAmount,
        totalAmount = totalAmount,
        label = label,
        discountRate = discountRate,
    )
