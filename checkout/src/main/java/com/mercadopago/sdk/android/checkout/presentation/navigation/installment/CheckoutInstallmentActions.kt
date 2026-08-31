package com.mercadopago.sdk.android.checkout.presentation.navigation.installment

import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.model.Screen
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams

internal class CheckoutInstallmentActions(
    val onOpenReview: (ProcessOrderParams) -> Unit,
    val onInstallmentConfirmed: (Int) -> Unit,
    val onBackClick: () -> Unit,
    val onFinishCheckout: (MercadoPagoCheckoutResult<*, *>) -> Unit,
    val onMarkScreenPresented: (Screen) -> Unit,
)
