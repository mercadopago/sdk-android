package com.mercadopago.sdk.android.checkout.domain.mapper

import com.mercadopago.sdk.android.checkout.domain.model.CardDataOutput
import com.mercadopago.sdk.android.checkout.domain.model.InstallmentsOutput
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.Quota
import com.mercadopago.sdk.android.checkout.domain.model.QuotaOutput
import com.mercadopago.sdk.android.checkout.domain.model.QuotaState
import com.mercadopago.sdk.android.checkout.domain.model.SelectionDisplayType

private const val QUOTA_STATE_SUCCESS = "success"
private const val SELECTION_TYPE_CHEVRON = "chevron"
private const val UNPROCESSED_ORDER_VALUE = ""

internal fun InstallmentsOutput.toMPInstallmentData(): MPInstallmentData =
    MPInstallmentData(
        quotas = quotas.map { it.toDomain() },
        display = MPInstallmentData.InstallmentDisplay(
            title = header.title,
            currencySymbol = footer.currencySymbol,
            displayType = selectionType.toDisplayType(),
            footer = MPInstallmentData.InstallmentFooterDisplay(
                footerTitle = footer.totalLabel,
                buttonLabel = footer.buttonLabel,
            ),
        ),
    )

internal fun CardDataOutput.toPendingPaymentData(): MPPaymentData.Payment =
    MPPaymentData.Payment(
        orderId = UNPROCESSED_ORDER_VALUE,
        orderStatus = UNPROCESSED_ORDER_VALUE,
        paymentMethodId = paymentMethodId,
        paymentTypeId = paymentTypeId,
    )

private fun QuotaOutput.toDomain(): Quota =
    Quota(
        installments = installments,
        installmentAmount = installmentAmount,
        totalAmount = totalAmount,
        primaryLabel = primaryLabel,
        secondaryLabel = secondaryLabel,
        tertiaryLabel = tertiaryLabel,
        state = state.toQuotaState(),
        accessibilityLabel = accessibilityLabel,
    )

private fun String.toQuotaState(): QuotaState =
    when (lowercase()) {
        QUOTA_STATE_SUCCESS -> QuotaState.Success
        else -> QuotaState.None
    }

private fun String.toDisplayType(): SelectionDisplayType =
    when (lowercase()) {
        SELECTION_TYPE_CHEVRON -> SelectionDisplayType.Chevron
        else -> SelectionDisplayType.RadioButton
    }
