package com.mercadopago.sdk.android.checkout.data.mapper

import com.mercadopago.sdk.android.checkout.presentation.extensions.toCurrencyString
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentState
import com.mercadopago.sdk.android.coremethods.domain.model.Installment
import com.mercadopago.sdk.android.coremethods.domain.model.PayerCost

internal const val SEPARATOR_INSTALLMENTS = "x"
internal const val EMPTY = ""
internal const val INTEREST_FREE = "Sem acréscimo"

internal const val FIRST_INSTALLMENT = 1

internal fun Installment?.toInstallmentsState(): List<InstallmentState> =
    this?.payerCost?.map {
        InstallmentState(
            text = "${it.instalments} $SEPARATOR_INSTALLMENTS ${it.installmentAmount?.toCurrencyString()}",
            description = "",
            trailing = it.formatTrailingText(),
            interestFree = it.installmentAmount == it.totalAmount,
            isSelected = false,
            number = it.instalments ?: FIRST_INSTALLMENT,
        )
    } ?: emptyList()

internal fun PayerCost.formatTrailingText(): String =
    when {
        instalments == FIRST_INSTALLMENT -> EMPTY
        installmentAmount == totalAmount -> INTEREST_FREE
        else -> totalAmount?.toCurrencyString().orEmpty()
    }
