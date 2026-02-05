package com.mercadopago.sdk.android.checkout.data.mapper

import com.mercadopago.sdk.android.checkout.presentation.extensions.toCurrencyString
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentState
import com.mercadopago.sdk.android.coremethods.domain.model.PayerCost

internal const val SEPARATOR_INSTALLMENTS = "x"
internal const val EMPTY = ""

internal fun List<PayerCost>.toInstallmentsState(): List<InstallmentState> =
    map {
        InstallmentState(
            text = "${it.instalments} $SEPARATOR_INSTALLMENTS ${it.installmentAmount?.toCurrencyString()}",
            description = "", // TECHDEBT
            trailing = "${it.totalAmount?.toCurrencyString()}",
            interestFree = false,
            isSelected = false,
            number = it.instalments ?: 1,
        )
    }
