package com.mercadopago.sdk.android.checkout.presentation.mapper

import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState
import java.math.BigDecimal

internal fun CardPaymentScreenState.toMPInstallmentData(
    transactionAmount: BigDecimal?,
): MPInstallmentData {
    val paymentMethodId = paymentState.paymentMethodId.orEmpty()
    val paymentTypeId = paymentState.paymentTypeId.orEmpty()
    return MPInstallmentData(
        brand = paymentMethodId,
        lastFourDigits = cardNumberState.lastFourDigits,
        paymentMethodId = paymentMethodId,
        paymentTypeId = paymentTypeId,
        issuerId = cardIssuers.firstOrNull()?.id,
        transactionAmount = transactionAmount,
        quotas = installmentsState.installments,
        display = MPInstallmentData.Display(
            displayType = installmentsState.displayType,
            title = installmentsState.title,
            totalLabel = installmentsState.totalLabel,
            payButtonLabel = installmentsState.payButtonLabel,
            currencySymbol = currencySymbol,
        ),
    )
}
