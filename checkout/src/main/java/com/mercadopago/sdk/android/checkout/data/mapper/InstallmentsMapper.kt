package com.mercadopago.sdk.android.checkout.data.mapper

import com.mercadopago.sdk.android.checkout.presentation.extensions.toCurrencyString
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentState
import com.mercadopago.sdk.android.coremethods.domain.model.Installment

internal const val SEPARATOR_INSTALLMENTS = "x"

internal fun Installment?.toInstallmentsState(): List<InstallmentState> =
    this?.payerCost?.map {
        InstallmentState(
            text = "${it.instalments} $SEPARATOR_INSTALLMENTS ${it.installmentAmount?.toCurrencyString()}",
            description = "",
            trailing = "${it.totalAmount?.toCurrencyString()}",
            interestFree = false,
            isSelected = false,
            number = it.instalments ?: 1,
        )
    } ?: emptyList()
