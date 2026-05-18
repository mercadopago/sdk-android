package com.mercadopago.sdk.android.checkout.data.remote.mapper

import com.mercadopago.sdk.android.checkout.data.remote.response.CardBinResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.QuotaResponse
import com.mercadopago.sdk.android.checkout.domain.mapper.toCardHolderField
import com.mercadopago.sdk.android.checkout.domain.mapper.toCardNumberField
import com.mercadopago.sdk.android.checkout.domain.mapper.toExpirationDateField
import com.mercadopago.sdk.android.checkout.domain.mapper.toSecurityCodeField
import com.mercadopago.sdk.android.checkout.domain.model.BinIssuer
import com.mercadopago.sdk.android.checkout.domain.model.CardBinData
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.Quota
import com.mercadopago.sdk.android.checkout.domain.model.QuotaState
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsDisplayType

internal fun CardBinResponse.toDomain(): CardBinData {
    val paymentMethod = paymentMethods?.firstOrNull()
    val installmentsTexts = translations?.installments
    return CardBinData(
        id = paymentMethod?.id,
        paymentTypeId = paymentMethod?.paymentTypeId,
        cardNumber = translations?.cardNumber?.let { paymentMethod?.cardNumber?.toCardNumberField(it) },
        securityCode = translations?.securityCode?.let { paymentMethod?.securityCode?.toSecurityCodeField(it) },
        holderName = translations?.holderName?.toCardHolderField(),
        expirationDate = translations?.expirationDate?.toExpirationDateField(),
        issuers = paymentMethod?.issuers?.map { BinIssuer(id = it.id?.toString(), name = it.name) } ?: emptyList(),
        installmentData = MPInstallmentData(
            quotas = installment?.quotas?.map { it.toDomain() } ?: emptyList(),
            display = MPInstallmentData.InstallmentDisplay(
                title = installmentsTexts?.header?.title.orEmpty(),
                currencySymbol = translations?.currencySymbol.orEmpty(),
                displayType = installment?.selectionType.toDisplayType(),
                footer = MPInstallmentData.InstallmentFooterDisplay(
                    footerTitle = installmentsTexts?.totalLabel.orEmpty(),
                    buttonLabel = installmentsTexts?.payButtonLabel.orEmpty(),
                ),
            ),
        ),
    )
}

private fun QuotaResponse.toDomain(): Quota =
    Quota(
        installments = installments,
        installmentAmount = installmentAmount?.toBigDecimal(),
        totalAmount = totalAmount?.toBigDecimal(),
        primaryLabel = primaryLabel,
        secondaryLabel = secondaryLabel,
        tertiaryLabel = tertiaryLabel,
        state = state.toQuotaState(),
    )

private fun String?.toQuotaState(): QuotaState =
    when (this?.lowercase()) {
        "success", "selected" -> QuotaState.Success
        else -> QuotaState.None
    }

private fun String?.toDisplayType(): InstallmentsDisplayType =
    if (equals("chevron", ignoreCase = true)) {
        InstallmentsDisplayType.Chevron
    } else {
        InstallmentsDisplayType.RadioButton
    }
