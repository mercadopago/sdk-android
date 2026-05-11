package com.mercadopago.sdk.android.checkout.presentation.mapper

import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.Quota
import com.mercadopago.sdk.android.checkout.presentation.extensions.getCurrencyString
import com.mercadopago.sdk.android.checkout.presentation.extensions.getTotal
import com.mercadopago.sdk.android.checkout.presentation.extensions.getTotalDecimalPart
import com.mercadopago.sdk.android.checkout.presentation.extensions.isInterestFree
import com.mercadopago.sdk.android.checkout.presentation.extensions.toBrandLabel
import com.mercadopago.sdk.android.checkout.presentation.extensions.toCurrencyString
import com.mercadopago.sdk.android.checkout.presentation.extensions.toInstallmentLabel
import com.mercadopago.sdk.android.checkout.presentation.state.FooterState
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentState
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsDisplayType
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsScreenState
import java.math.BigDecimal

private const val FIRST_INSTALLMENT = 1

internal fun MPInstallmentData.toInstallmentsScreenState(): InstallmentsScreenState {
    val title = when (display.displayType) {
        InstallmentsDisplayType.Chevron -> display.headerChevron
        InstallmentsDisplayType.RadioButton -> display.headerRadio
    }
    val items = quotas
        .toInstallmentStates(interestFreeLabel = display.interestFreeLabel)
        .applySelection(displayType = display.displayType, selectedNumber = selectedInstallment)
    val amount = transactionAmount ?: BigDecimal.ZERO
    return InstallmentsScreenState(
        title = title,
        displayType = display.displayType,
        installmentsState = items,
        footerState = FooterState(
            title = display.totalLabel,
            currencySymbol = null.getCurrencyString(),
            amountIntegerPart = amount.getTotal(),
            amountDecimalPart = amount.getTotalDecimalPart(),
            subtitle = toSubtitle(),
            buttonLabel = display.payButtonLabel
                .takeIf { display.displayType == InstallmentsDisplayType.RadioButton && it.isNotEmpty() },
        ),
    )
}

private fun List<Quota>.toInstallmentStates(
    interestFreeLabel: String,
): List<InstallmentState> =
    map { quota ->
        val isInterestFree = quota.isInterestFree()
        InstallmentState(
            text = quota.toInstallmentLabel(),
            description = "",
            trailing = when {
                quota.installments == FIRST_INSTALLMENT -> ""
                isInterestFree -> interestFreeLabel
                else -> quota.totalAmount?.toCurrencyString().orEmpty()
            },
            interestFree = isInterestFree,
            isSelected = false,
            number = quota.installments ?: FIRST_INSTALLMENT,
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

private fun MPInstallmentData.toSubtitle(): String =
    listOf(brand.toBrandLabel(), "****", lastFourDigits)
        .filter { it.isNotEmpty() }
        .joinToString(separator = " ")
