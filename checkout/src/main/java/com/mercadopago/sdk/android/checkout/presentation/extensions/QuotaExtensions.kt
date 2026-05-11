package com.mercadopago.sdk.android.checkout.presentation.extensions

import com.mercadopago.sdk.android.checkout.domain.model.Quota

private const val INSTALLMENTS_SEPARATOR = "x"

internal fun Quota.toInstallmentLabel(): String {
    val amount = installmentAmount?.toCurrencyString().orEmpty()
    return "$installments $INSTALLMENTS_SEPARATOR $amount"
}

internal fun Quota.isInterestFree(): Boolean =
    installmentAmount != null &&
        totalAmount != null &&
        installmentAmount.compareTo(totalAmount) == 0
