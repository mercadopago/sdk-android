package com.mercadopago.sdk.android.checkout.domain.mapper

import com.mercadopago.sdk.android.checkout.domain.model.InstallmentsOutput
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.Quota
import com.mercadopago.sdk.android.checkout.domain.model.QuotaOutput
import com.mercadopago.sdk.android.checkout.domain.model.QuotaState
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsDisplayType

/**
 * Converts `InstallmentsOutput` (from `/initialization`) to `MPInstallmentData` for the
 * existing installments screen. BFF-formatted labels are used directly — SDK does not
 * reformat amounts for this flow.
 */
internal fun InstallmentsOutput.toInstallmentData(): MPInstallmentData =
    MPInstallmentData(
        quotas = quotas.map { it.toQuota() },
        display = MPInstallmentData.InstallmentDisplay(
            title = header.title,
            currencySymbol = "",
            displayType = if (selectionType == "chevron") {
                InstallmentsDisplayType.Chevron
            } else {
                InstallmentsDisplayType.RadioButton
            },
            footer = MPInstallmentData.InstallmentFooterDisplay(
                footerTitle = totalLabel,
                buttonLabel = payButtonLabel,
            ),
        ),
    )

private fun QuotaOutput.toQuota(): Quota =
    Quota(
        installments = installments,
        installmentAmount = installmentAmount,
        totalAmount = totalAmount,
        primaryLabel = primaryLabel,
        secondaryLabel = secondaryLabel,
        state = state.toQuotaState(),
    )

private fun String.toQuotaState(): QuotaState =
    when (lowercase()) {
        "interest_free", "success", "selected" -> QuotaState.Success
        else -> QuotaState.None
    }
