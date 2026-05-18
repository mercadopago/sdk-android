package com.mercadopago.sdk.android.checkout.presentation.mapper

import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.Quota
import com.mercadopago.sdk.android.checkout.domain.model.QuotaState
import com.mercadopago.sdk.android.checkout.presentation.extensions.getTotal
import com.mercadopago.sdk.android.checkout.presentation.extensions.getTotalDecimalPart
import com.mercadopago.sdk.android.checkout.presentation.extensions.toAmountParts
import com.mercadopago.sdk.android.checkout.presentation.extensions.toBrandLabel
import com.mercadopago.sdk.android.checkout.presentation.state.AmountParts
import com.mercadopago.sdk.android.checkout.presentation.state.FooterState
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentState
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsDisplayType
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsScreenState
import java.math.BigDecimal

internal fun MPInstallmentData.toInstallmentsScreenState(): InstallmentsScreenState {
    val selectedNumber = selectedInstallment ?: quotas.preselected()
    val items = quotas
        .toInstallmentStates()
        .applySelection(displayType = display.displayType, selectedNumber = selectedNumber)
    val amount = footerAmount(effectiveSelectedQuota(selectedNumber))
    return InstallmentsScreenState(
        title = display.title,
        displayType = display.displayType,
        items = items,
        footerState = FooterState(
            title = display.totalLabel,
            currencySymbol = amount.currencySymbol,
            amountIntegerPart = amount.integerPart,
            amountDecimalPart = amount.decimalPart,
            subtitle = toSubtitle(),
            buttonLabel = display.buttonLabel.takeIf {
                display.displayType == InstallmentsDisplayType.RadioButton && it.isNotEmpty()
            },
            isVisible = true,
        ),
    )
}

private fun List<Quota>.toInstallmentStates(): List<InstallmentState> =
    map { quota ->
        InstallmentState(
            text = quota.primaryLabel.orEmpty(),
            trailing = quota.secondaryLabel.orEmpty(),
            description = quota.tertiaryLabel.orEmpty(),
            isSelected = false,
            number = quota.installments ?: 1,
        )
    }

private fun List<InstallmentState>.applySelection(
    displayType: InstallmentsDisplayType,
    selectedNumber: Int?,
): List<InstallmentState> =
    when {
        displayType != InstallmentsDisplayType.RadioButton -> this
        selectedNumber != null -> map { it.copy(isSelected = it.number == selectedNumber) }
        else -> mapIndexed { index, item -> item.copy(isSelected = index == 0) }
    }

private fun List<Quota>.preselected(): Int? = firstOrNull { it.state == QuotaState.Success }?.installments

private fun MPInstallmentData.effectiveSelectedQuota(
    selectedNumber: Int?,
): Quota? =
    quotas.firstOrNull { it.installments == selectedNumber }
        ?: quotas.firstOrNull().takeIf { display.displayType == InstallmentsDisplayType.RadioButton }

private fun MPInstallmentData.footerAmount(
    selectedQuota: Quota?,
): AmountParts {
    val symbol = display.currencySymbol
    selectedQuota?.secondaryLabel
        ?.takeIf { it.isNotEmpty() }
        ?.let { return it.toAmountParts(symbol) }
    val total = selectedQuota?.totalAmount ?: transactionAmount ?: BigDecimal.ZERO
    return AmountParts(
        currencySymbol = symbol,
        integerPart = total.getTotal(),
        decimalPart = total.getTotalDecimalPart(),
    )
}

private fun MPInstallmentData.toSubtitle(): String =
    listOf(brand.toBrandLabel(), "****", lastFourDigits)
        .filter { it.isNotEmpty() }
        .joinToString(separator = " ")
