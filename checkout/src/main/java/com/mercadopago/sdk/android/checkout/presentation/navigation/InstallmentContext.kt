package com.mercadopago.sdk.android.checkout.presentation.navigation

import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeScreenConfig

internal class InstallmentContext(
    val installmentData: MPInstallmentData,
    val paymentData: MPPaymentData,
    val origin: ReviewOrigin,
    val securityCodeConfig: SecurityCodeScreenConfig? = null,
)
