package com.mercadopago.sdk.android.checkout.data.remote.mapper

import com.mercadopago.sdk.android.checkout.data.remote.response.CardBinResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.IssuerResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.QuotaResponse
import com.mercadopago.sdk.android.checkout.domain.model.BinIssuer
import com.mercadopago.sdk.android.checkout.domain.model.CardBinData
import com.mercadopago.sdk.android.checkout.domain.model.Quota
import com.mercadopago.sdk.android.checkout.domain.model.QuotaState

private const val QUOTA_STATE_SELECTED = "selected"
private const val QUOTA_STATE_DISABLED = "disabled"

internal fun CardBinResponse.toDomain(): CardBinData {
    val paymentMethod = paymentMethods?.firstOrNull()
    return CardBinData(
        id = paymentMethod?.id,
        paymentTypeId = paymentMethod?.paymentTypeId,
        cardNumber = paymentMethod?.cardNumber,
        securityCode = paymentMethod?.securityCode,
        issuers = paymentMethod?.issuers?.map { it.toDomain() } ?: emptyList(),
        quotas = installment?.quotas?.map { it.toDomain() } ?: emptyList(),
        installmentsSelectionType = installment?.selectionType,
        translations = translations,
    )
}

private fun IssuerResponse.toDomain(): BinIssuer =
    BinIssuer(
        id = id,
        name = name,
    )

private fun QuotaResponse.toDomain(): Quota =
    Quota(
        installments = installments,
        installmentAmount = installmentAmount?.toBigDecimal(),
        totalAmount = totalAmount?.toBigDecimal(),
        primaryLabel = primaryLabel,
        secondaryLabel = secondaryLabel,
        state = state.toQuotaState(),
    )

private fun String?.toQuotaState(): QuotaState =
    when (this?.lowercase()) {
        QUOTA_STATE_SELECTED -> QuotaState.Selected
        QUOTA_STATE_DISABLED -> QuotaState.Disabled
        else -> QuotaState.None
    }
