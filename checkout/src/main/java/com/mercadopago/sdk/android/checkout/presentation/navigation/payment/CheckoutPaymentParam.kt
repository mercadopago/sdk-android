package com.mercadopago.sdk.android.checkout.presentation.navigation.payment

import com.mercadopago.sdk.android.checkout.presentation.navigation.PaymentFeedbackEvent
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.PaymentBrickViewModel

internal class CheckoutPaymentParam(
    val viewModel: PaymentBrickViewModel,
    val feedback: PaymentFeedbackEvent?,
)
