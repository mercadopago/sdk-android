package com.mercadopago.sdk.android.checkout.presentation.navigation.installment

import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardPaymentViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.PaymentBrickViewModel

internal class CheckoutInstallmentParam(
    val installmentData: MPInstallmentData,
    val paymentData: MPPaymentData,
    val checkoutConfiguration: CheckoutConfiguration?,
    val cardPaymentViewModel: CardPaymentViewModel,
    val paymentBrickViewModel: PaymentBrickViewModel,
)
