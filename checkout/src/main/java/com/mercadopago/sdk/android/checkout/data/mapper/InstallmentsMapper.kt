package com.mercadopago.sdk.android.checkout.data.mapper

import com.mercadopago.sdk.android.checkout.presentation.extensions.toCurrencyString
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentState
import com.mercadopago.sdk.android.coremethods.domain.model.PayerCost
import java.math.BigDecimal
import java.math.RoundingMode

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

internal fun BigDecimal.getTotal(): String {
    val scaled = setScale(2, RoundingMode.HALF_UP)
    return scaled.toBigInteger().toString()
}

internal fun BigDecimal.getTotalDecimalPart(): String {
    val scaled = setScale(2, RoundingMode.HALF_UP)
    val cents = scaled.remainder(BigDecimal.ONE)
        .movePointRight(2)
        .abs()
        .toInt()
    return cents.toString().padStart(2, '0')
}
