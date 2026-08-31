package com.mercadopago.sdk.android.checkout.presentation.navigation.payment

import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionScreenData
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.checkout.presentation.navigation.PaymentFeedback
import com.mercadopago.sdk.android.checkout.presentation.navigation.PaymentFeedbackEvent
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeScreenConfig

@Suppress("LongParameterList")
internal class CheckoutPaymentActions(
    val onOpenForm: () -> Unit,
    val onOpenSecurityCode: (SecurityCodeScreenConfig) -> Unit,
    val onOpenReview: (ProcessOrderParams) -> Unit,
    val onFinishCheckout: (MercadoPagoCheckoutResult<*, *>) -> Unit,
    val onOpenOfflineMethodSelector: (MethodSelectionScreenData) -> Unit,
    val onShowFeedback: (PaymentFeedback) -> Unit,
    val onFeedbackConsumed: (PaymentFeedbackEvent) -> Unit,
)
