package com.mercadopago.sdk.android.checkout.presentation.navigation

import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.checkout.presentation.state.CardFormViewEvent
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentBrickViewEvent

internal class OrderProcessedActions(
    val onFinishWithProcessedOrder: (
        MercadoPagoCheckoutResult<*, *>,
        ProcessOrderParams,
        OrderProcessOutput.PaymentProcessed?,
    ) -> Unit,
    val onNavigateToStatusScreenForResolution: (
        ProcessOrderParams,
        MercadoPagoCheckoutError,
        String?,
    ) -> Unit,
) {
    fun handle(
        event: PaymentBrickViewEvent.OnOrderProcessed,
        onConsumed: () -> Unit,
    ) {
        onConsumed()
        onFinishWithProcessedOrder(
            MercadoPagoCheckoutResult.Success(event.payment),
            event.params,
            event.paymentProcessed,
        )
    }

    fun handle(
        event: PaymentBrickViewEvent.OnOrderProcessFailed,
        onConsumed: () -> Unit,
    ) {
        onConsumed()
        onNavigateToStatusScreenForResolution(event.params, event.error, event.statusDetail)
    }

    fun handle(
        event: CardFormViewEvent.OnOrderProcessed,
        onConsumed: () -> Unit,
    ) {
        onConsumed()
        onFinishWithProcessedOrder(
            MercadoPagoCheckoutResult.Success(event.payment),
            event.params,
            event.paymentProcessed,
        )
    }

    fun handle(
        event: CardFormViewEvent.OnOrderProcessFailed,
        onConsumed: () -> Unit,
    ) {
        onConsumed()
        onNavigateToStatusScreenForResolution(event.params, event.error, event.statusDetail)
    }
}
