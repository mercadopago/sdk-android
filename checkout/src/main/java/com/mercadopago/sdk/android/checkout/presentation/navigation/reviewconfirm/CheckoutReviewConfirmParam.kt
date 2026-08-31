package com.mercadopago.sdk.android.checkout.presentation.navigation.reviewconfirm

import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.presentation.navigation.ReviewContext
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardPaymentViewModel

internal class CheckoutReviewConfirmParam(
    val reviewContext: ReviewContext,
    val checkoutConfiguration: CheckoutConfiguration?,
    val cardPaymentViewModel: CardPaymentViewModel?,
)
