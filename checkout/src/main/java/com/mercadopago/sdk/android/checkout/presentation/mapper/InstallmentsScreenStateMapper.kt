package com.mercadopago.sdk.android.checkout.presentation.mapper

import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.Quota
import com.mercadopago.sdk.android.checkout.domain.model.QuotaState
import com.mercadopago.sdk.android.checkout.presentation.extensions.getCurrencyString
import com.mercadopago.sdk.android.checkout.presentation.extensions.getTotal
import com.mercadopago.sdk.android.checkout.presentation.extensions.getTotalDecimalPart
import com.mercadopago.sdk.android.checkout.presentation.extensions.toBrandLabel
import com.mercadopago.sdk.android.checkout.presentation.extensions.toInstallmentLabel
import com.mercadopago.sdk.android.checkout.presentation.state.FooterState
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentState
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsDisplayType
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsScreenState
import java.math.BigDecimal

private const val FIRST_INSTALLMENT = 1
private val CURRENCY_SYMBOL_REGEX = Regex("""([^\d\s]+)\s+[\d.,]""")

internal fun MPInstallmentData.toInstallmentsScreenState(): InstallmentsScreenState {
    val effectiveSelectedNumber = selectedInstallment ?: quotas.preselected()
    val items = quotas
        .toInstallmentStates()
        .applySelection(displayType = display.displayType, selectedNumber = effectiveSelectedNumber)
    val amount = effectiveSelectedQuota(effectiveSelectedNumber)?.totalAmount
        ?: transactionAmount
        ?: BigDecimal.ZERO
    return InstallmentsScreenState(
        title = display.title,
        displayType = display.displayType,
        installmentsState = items,
        footerState = FooterState(
            title = display.totalLabel,
            currencySymbol = quotas.extractCurrencySymbol() ?: null.getCurrencyString(),
            amountIntegerPart = amount.getTotal(),
            amountDecimalPart = amount.getTotalDecimalPart(),
            subtitle = toSubtitle(),
            buttonLabel = display.payButtonLabel
                .takeIf { display.displayType == InstallmentsDisplayType.RadioButton && it.isNotEmpty() },
        ),
    )
}

private fun List<Quota>.toInstallmentStates(): List<InstallmentState> =
    map { quota ->
        InstallmentState(
            text = quota.primaryLabel.orEmpty().ifEmpty { quota.toInstallmentLabel() },
            trailing = quota.secondaryLabel.orEmpty(),
            description = quota.tertiaryLabel.orEmpty(),
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

private fun List<Quota>.preselected(): Int? = firstOrNull { it.state == QuotaState.Selected }?.installments

private fun List<Quota>.extractCurrencySymbol(): String? =
    firstNotNullOfOrNull { quota ->
        val source = listOf(quota.secondaryLabel, quota.primaryLabel)
            .firstOrNull { !it.isNullOrEmpty() }
            .orEmpty()
        CURRENCY_SYMBOL_REGEX.findAll(source)
            .mapNotNull { it.groupValues.getOrNull(1)?.trim() }
            .firstOrNull { symbol -> symbol.isNotEmpty() && symbol.any { !it.isLetter() } }
    }

private fun MPInstallmentData.effectiveSelectedQuota(
    selectedNumber: Int?,
): Quota? =
    quotas.firstOrNull { it.installments == selectedNumber }
        ?: quotas.firstOrNull().takeIf { display.displayType == InstallmentsDisplayType.RadioButton }

private fun MPInstallmentData.toSubtitle(): String =
    listOf(brand.toBrandLabel(), "****", lastFourDigits)
        .filter { it.isNotEmpty() }
        .joinToString(separator = " ")
