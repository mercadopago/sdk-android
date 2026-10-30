package com.mercadopago.sdk.android.checkout.presentation.navigation.reviewconfirm

import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.checkout.presentation.navigation.ReviewOrigin

@Suppress("LongParameterList")
internal class CheckoutReviewConfirmActions(
    val onNavigateUp: (ReviewOrigin) -> Unit,
    val onReturnToPaymentSelector: () -> Unit,
    val onReturnToPaymentSelectorWithGenericError: () -> Unit,
    val onReturnToCardTransactionWithGenericError: () -> Unit,
    val onNavigateToStatusScreenForResolution: (ProcessOrderParams, MercadoPagoCheckoutError?, String?) -> Unit,
    val onFinishCheckout: (
        MercadoPagoCheckoutResult<*, *>,
        ProcessOrderParams?,
        OrderProcessOutput.PaymentProcessed?,
    ) -> Unit,
    val onFinishForEmailChange: ((() -> Unit)?) -> Unit,
)
