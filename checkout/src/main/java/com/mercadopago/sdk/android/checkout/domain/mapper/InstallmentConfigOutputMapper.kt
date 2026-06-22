package com.mercadopago.sdk.android.checkout.domain.mapper

import com.mercadopago.sdk.android.checkout.domain.model.CardQuotaOutput
import com.mercadopago.sdk.android.checkout.domain.model.CardTranslationsOutput
import com.mercadopago.sdk.android.checkout.domain.model.InstallmentConfigOutput
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.Quota
import com.mercadopago.sdk.android.checkout.domain.model.QuotaState
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsDisplayType

/**
 * Converts [InstallmentConfigOutput] (from `GET /payment_brick/card`) to [MPInstallmentData]
 * for the existing installments screen.
 *
 * @param translations Card translations used to populate display labels. BFF-formatted labels
 * from quotas are used directly — SDK does not reformat amounts.
 */
internal fun InstallmentConfigOutput.toInstallmentData(
    translations: CardTranslationsOutput,
): MPInstallmentData =
    MPInstallmentData(
        quotas = quotas.map { it.toQuota() },
        display = MPInstallmentData.InstallmentDisplay(
            title = translations.installments.header.title,
            currencySymbol = "",
            displayType = if (selectionType == "chevron") {
                InstallmentsDisplayType.Chevron
            } else {
                InstallmentsDisplayType.RadioButton
            },
            footer = MPInstallmentData.InstallmentFooterDisplay(
                footerTitle = translations.installments.totalLabel,
                buttonLabel = translations.cardFormFooterButtonLabel,
            ),
        ),
    )

private fun CardQuotaOutput.toQuota(): Quota =
    Quota(
        installments = installments,
        installmentAmount = installmentAmount,
        totalAmount = totalAmount,
        primaryLabel = primaryLabel,
        secondaryLabel = secondaryLabel,
        accessibilityLabel = accessibilityLabel,
        state = state.toQuotaState(),
    )

private fun String.toQuotaState(): QuotaState =
    when (lowercase()) {
        "interest_free", "success", "selected", "recommended" -> QuotaState.Success
        else -> QuotaState.None
    }
